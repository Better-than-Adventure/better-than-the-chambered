rootProject.name = "better-than-the-chambered"
include("core")
include("mods")
include("mods:prelude")
findProject(":mods:prelude")?.name = "prelude"
include("mods:wolf3d")
findProject(":mods:wolf3d")?.name = "wolf3d"
