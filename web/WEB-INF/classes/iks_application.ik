
"LOADING: my first Ioke web application!" println

IKanServe actionForPath(#r[^/foo], method("hello world!!"))
IKanServe actionForPath(#r[^/bar], method("you requested this: #{request pathInfo}"))
