
IKanServe = Origin mimic ; module
IKanServe Request = Origin mimic

use("ikanserve/actions")
use("ikanserve/dispatch")
use("ikanserve/html")

bind(handle(Condition Error Load, fn(c, "no application defined" println. invokeRestart(:ignoreLoadError))),
  use("iks_application.ik")
)
