
IKanServe = Origin mimic ; module
IKanServe Request = Origin mimic
IKanServe valuesFromRequest = method(r, namesName, valuesName, 
  out = {}
  
  enm = r send(namesName)
  while(enm hasMoreElements,
    vName = enm nextElement
    values = []
    enm2 = r send(valuesName, vName)
    while(enm2 hasMoreElements,
      values << enm2 nextElement asText
    )
    out[vName asText] = values
  )

  out
)

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

IKanServe dispatch = method(
  ; what should dispatch do?
  ;  put all the request parameters in a dict
  ;  dispatch based on the registered dispatch scenarios from the url
  ;  render the result of calling the thing. it should return a text
  ;
  req = Request mimic
  req headers = valuesFromRequest(request, :headerNames, :headers)
  req params = {}

  req contextPath = request contextPath asText
  req requestMethod = request getMethod asText
  req pathInfo = request pathInfo asText
  req pathTranslated = request pathTranslated asText
  req queryString = request queryString asText
  req remoteUser = request remoteUser asText
  req requestURI = request requestURI asText
  req requestURL = request requestURL asText
  req servletPath = request servletPath asText

  req params = {}
  enm = request parameterNames
  while(enm hasMoreElements,
    vName = enm nextElement
    req params[vName asText] = request parameter(vName) asText
  )

  action = findAction(req) mimic
  action request = req
  action response = response
  result = action dispatch

  response writer write(result)
  response flushBuffer
)

bind(handle(Condition Error Load, fn(c, "no application defined" println. invokeRestart(:ignoreLoadError))),
  use("iks_application.ik")
)
