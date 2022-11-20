package com.mojang.escape.gui

import com.mojang.escape.Art
import com.mojang.escape.Game
import com.mojang.escape.level.Level
import java.util.*
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

class Bitmap3D(width: Int, height: Int): Bitmap(width, height) {
    private val zBuffer = DoubleArray(width * height)
    private val zBufferWall = DoubleArray(width)

    private var xCam = 0.0
    private var yCam = 0.0
    private var zCam = 0.0
    private var rCos = 0.0
    private var rSin = 0.0
    private var fov = 0.0
    private var xCenter = 0.0
    private var yCenter = 0.0
    private var rot = 0.0

    fun render(game: Game) {
        val player = game.player!!
        val level = game.level!!

        Arrays.setAll(zBufferWall) {
            0.0
        }
        Arrays.setAll(zBuffer) {
            10000.0
        }

        rot = player.rot
        xCam = player.x - sin(rot) * 0.3
        yCam = player.z - cos(rot) * 0.3
        zCam = -0.2 + sin(player.bobPhase * 0.4) * 0.01 * player.bob - player.y

        xCenter = width / 2.0
        yCenter = height / 3.0

        rCos = cos(rot)
        rSin = sin(rot)

        fov = height.toDouble()

        val r = 6
        val xCenter = floor(xCam).toInt()
        val zCenter = floor(yCam).toInt()
        for (zb in (zCenter - r)..(zCenter + r)) {
            for (xb in (xCenter - r)..(xCenter + r)) {
                if (xb < 0 || zb >= level.width || zb < 0 || zb >= level.height) continue
                
                val c = level.getBlock(xb, zb)
                val e = level.getBlock(xb + 1, zb)
                val s = level.getBlock(xb, zb + 1)
                
                if (!c.doRender(this, xb, zb, c, e, s)) {
                    if (c.solidRender) {
                        if (!e.solidRender) {
                            renderWall(xb + 1.0, zb + 1.0, xb + 1.0, zb + 0.0, c.tex, c.col)
                        }
                        if (!s.solidRender) {
                            renderWall(xb + 0.0, zb + 1.0, xb + 1.0, zb + 1.0, c.tex, (c.col and 0xFEFEFE) shr 1)
                        }
                    } else {
                        if (e.solidRender) {
                            renderWall(xb + 1.0, zb + 0.0, xb + 1.0, zb + 1.0, e.tex, e.col)
                        }
                        if (s.solidRender) {
                            renderWall(xb + 1.0, zb + 1.0, xb + 0.0, zb + 1.0, s.tex, (s.col and 0xFEFEFE) shr 1)
                        }
                    }
                }

            }
        }

        for (zb in (zCenter - r)..(zCenter + r)) {
            for (xb in (xCenter - r)..(xCenter + r)) {
                val c = level.getBlock(xb, zb)

                for (e in c.entities) {
                    for (sprite in e.sprites) {
                        renderSprite(e.x + sprite.x, 0 - sprite.y, e.z + sprite.z, sprite.tex, sprite.col)
                    }
                }

                for (sprite in c.sprites) {
                    renderSprite(xb + sprite.x, 0 - sprite.y, zb + sprite.z, sprite.tex, sprite.col)
                }
            }
        }

        renderFloor(level)
    }

    fun renderFloor(level: Level) {
        for (y in 0 until height) {
            val yd = ((y + 0.5) - yCenter) / fov

            var floor = true
            var zd = (4 - zCam * 8) / yd
            if (yd < 0) {
                floor = false
                zd = (4 + zCam * 8) / -yd
            }

            for (x in 0 until width) {
                if (zBuffer[x + y * width] <= zd) {
                    continue
                }

                var xd = (xCenter - x) / fov
                xd *= zd

                val xx = xd * rCos + zd * rSin + (xCam + 0.5) * 8
                val yy = zd * rCos - xd * rSin + (yCam + 0.5) * 8

                val xPix = (xx * 2).toInt()
                val yPix = (yy * 2).toInt()
                val xTile = xPix shr 4
                val yTile = yPix shr 4

                val block = level.getBlock(xTile, yTile)
                var col = block.floorCol
                var tex = block.floorTex
                if (!floor) {
                    col = block.ceilCol
                    tex = block.ceilTex
                }

                if (tex < 0) {
                    zBuffer[x + y * width] = -1.0
                } else {
                    zBuffer[x + y * width] = zd
                    pixels[x + y * width] = Art.floors.pixels[((xPix and 15) + (tex % 8) * 16) + ((yPix and 15) + (tex / 8) * 16) * 128] * col
                }
            }
        }
    }

    fun renderSprite(x: Double, y: Double, z: Double, tex: Int, color: Int) {
        val xc = (x - xCam) * 2 - rSin * 0.2
        val yc = (y - zCam) * 2
        val zc = (z - yCam) * 2 - rCos * 0.2

        val xx = xc * rCos - zc * rSin
        val yy = yc
        var zz = zc * rCos + xc * rSin

        if (zz < 0.1) return

        val xPixel = xCenter - (xx / zz * fov)
        val yPixel = (yy / zz * fov + yCenter)

        val xPixel0 = xPixel - height / zz
        val xPixel1 = xPixel + height / zz

        val yPixel0 = yPixel - height / zz
        val yPixel1 = yPixel + height / zz

        var xp0 = ceil(xPixel0).toInt()
        var xp1 = ceil(xPixel1).toInt()
        var yp0 = ceil(yPixel0).toInt()
        var yp1 = ceil(yPixel1).toInt()

        if (xp0 < 0){
            xp0 = 0
        }
        if (xp1 > width) {
            xp1 = width
        }
        if (yp0 < 0) {
            yp0 = 0
        }
        if (yp1 > height) {
            yp1 = height
        }
        zz *= 4
        for (yp in yp0 until yp1) {
            val ypr = (yp - yPixel0) / (yPixel1 - yPixel0)
            val yt = (ypr * 16).toInt()
            for (xp in xp0 until xp1) {
                val xpr = (xp - xPixel0) / (xPixel1 - xPixel0)
                val xt = (xpr * 16).toInt()
                if (zBuffer[xp + yp * width] > zz) {
                    val col = Art.sprites.pixels[(xt + tex % 8 * 16) + (yt + (tex / 8) * 16) * 128]
                    if (col >= 0) {
                        pixels[xp + yp * width] = col * color
                        zBuffer[xp + yp * width] = zz
                    }
                }
            }
        }
    }

    fun renderWall(x0: Double, y0: Double, x1: Double, y1: Double, tex: Int, color: Int) {
        renderWall(x0, y0, x1, y1, tex, color, 0.0, 1.0)
    }

    fun renderWall(x0: Double, y0: Double, x1: Double, y1: Double, tex: Int, color: Int, xt0: Double, xt1: Double) {
        val xc0 = ((x0 - 0.5) - xCam) * 2
        val yc0 = ((y0 - 0.5) - yCam) * 2

        var xx0 = xc0 * rCos - yc0 * rSin
        val u0 = ((-0.5) - zCam) * 2
        val l0 = ((+0.5) - zCam) * 2
        var zz0 = yc0 * rCos + xc0 * rSin

        val xc1 = ((x1 - 0.5) - xCam) * 2
        val yc1 = ((y1 - 0.5) - yCam) * 2

        var xx1 = xc1 * rCos - yc1 * rSin
        val u1 = ((-0.5) - zCam) * 2
        val l1 = ((+0.5) - zCam) * 2
        var zz1 = yc1 * rCos + xc1 * rSin

        var xt0 = xt0 * 16
        var xt1 = xt1 * 16

        val zClip = 0.2

        if (zz0 < zClip && zz1 < zClip) {
            return
        }

        if (zz0 < zClip) {
            val p = (zClip - zz0) / (zz1 - zz0)
            zz0 += (zz1 - zz0) * p
            xx0 += (xx1 - xx0) * p
            xt0 += (xt1 - xt0) * p
        }

        if (zz1 < zClip) {
            val p = (zClip - zz0) / (zz1 - zz0)
            zz1 = zz0 + (zz1 - zz0) * p
            xx1 = xx0 + (xx1 - xx0) * p
            xt1 = xt0 + (xt1 - xt0) * p
        }

        val xPixel0 = xCenter - (xx0 / zz0 * fov)
        val xPixel1 = xCenter - (xx1 / zz1 * fov)

        if (xPixel0 >= xPixel1) {
            return
        }
        var xp0 = ceil(xPixel0).toInt()
        var xp1 = ceil(xPixel1).toInt()
        if (xp0 < 0) {
            xp0 = 0
        }
        if (xp1 > width) {
            xp1 = width
        }

        val yPixel00 = (u0 / zz0 * fov + yCenter)
        val yPixel01 = (l0 / zz0 * fov + yCenter)
        val yPixel10 = (u1 / zz1 * fov + yCenter)
        val yPixel11 = (l1 / zz1 * fov + yCenter)

        val iz0 = 1 / zz0
        val iz1 = 1 / zz1

        val iza = iz1 - iz0

        val ixt0 = xt0 * iz0
        val ixta = xt1 * iz1 - ixt0
        val iw = 1 / (xPixel1 - xPixel0)

        for (x in xp0 until xp1) {
            val pr = (x - xPixel0) * iw
            val iz = iz0 + iza * pr

            if (zBufferWall[x] > iz) continue
            zBufferWall[x] = iz
            val xTex = ((ixt0 + ixta * pr) / iz).toInt()

            val yPixel0 = yPixel00 + (yPixel10 - yPixel00) * pr - 0.5
            val yPixel1 = yPixel01 + (yPixel11 - yPixel01) * pr

            var yp0 = ceil(yPixel0).toInt()
            var yp1 = ceil(yPixel1).toInt()
            if (yp0 < 0) {
                yp0 = 0
            }
            if (yp1 > height) {
                yp1 = height
            }

            val ih = 1 / (yPixel1 - yPixel0)
            for (y in yp0 until yp1) {
                val pry = (y - yPixel0) * ih
                val yTex = (16 * pry).toInt()
                pixels[x + y * width] = Art.walls.pixels[((xTex) + (tex % 8) * 16) + (yTex + tex / 8 * 16) * 128] * color
                zBuffer[x + y * width] = 1 / iz * 4
            }
        }
    }

    fun postProcess(level: Level) {
        for (i in 0 until (width * height)) {
            val zl = zBuffer[i]
            if (zl < 0) {
                val xx = (floor((i % width) - rot * 512 / (Math.PI * 2)).toInt()) and 511
                val yy = i / width
                if (xx + yy * 512 < Art.sky.pixels.size) pixels[i] = Art.sky.pixels[xx + yy * 512] * 0x444455
            } else {
                val xp = (i % width)
                val yp = (i / width) * 14

                val xx = ((i % width - width / 2.0) / width)
                val col = pixels[i]
                var brightness = (300 - zl * 6 * (xx * xx * 2 + 1)).toInt()
                brightness = (brightness + ((xp + yp) and 3) * 4) shr 4 shl 4
                if (brightness < 0) {
                    brightness = 0
                }
                if (brightness > 255) {
                    brightness = 255
                }

                var r = (col shr 16) and 0xFF
                var g = (col shr 8)  and 0xFF
                var b = (col shr 0)  and 0xFF

                r = r * brightness / 255
                g = g * brightness / 255
                b = b * brightness / 255

                pixels[i] = (r shl 16) or (g shl 8) or (b shl 0)
            }
        }
    }
}