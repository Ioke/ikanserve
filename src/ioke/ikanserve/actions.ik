IKanServe PathAction = Origin mimic do(
  === = method(req, self regexp =~ req pathInfo)
)

IKanServe actionForPath = method(regexp, action,
  pa = PathAction mimic
  "Registering action for: #{regexp pattern}" println
  pa regexp = regexp
  pa dispatch = cell(:action)
  IKanServe Actions << pa
  pa
)

IKanServe NothingFound = Origin mimic do(
  === = method(other, true)

  dispatch = method(
    response setStatus(404)

    "Couldn't find any action corresponding to your request. Sorry!"
  )
)

IKanServe Actions = []

IKanServe findAction = method(req,
  Actions find(=== req) || IKanServe NothingFound
)
