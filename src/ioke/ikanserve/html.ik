
;IKanServe = Origin mimic
IKanServe HtmlBuilder = Origin mimic do(
  initialize = method(arguments, call,
    @arguments = arguments
    @call = call
  )

  build = method(
    buildTag("html", "%[%s%]" format(arguments map(x, buildPossibleTag(x))))
  )

  buildTag = method(name, content, attributes {},
    "<#{name}>#{content}</#{name}>\n"
  )

  buildPossibleTag = method(arg,
    while(arg && arg terminator?,
      arg = arg next)
    if(arg,
      name = arg name
      if(#/^h(tml)?:({tagName}.*)$/ =~ name,
        args = arg arguments
        
        result = buildTag(it tagName, "%[%s%]" format(args map(x, buildPossibleTag(x))))
        if(arg next,
          result + buildPossibleTag(it),
          result),
        arg evaluateOn(call ground, call ground)
      )
      ,
      ""
    )
  )
)

IKanServe html = macro("basically, this will walk all arguments, putting together an html document from it. it will not evaluate anything that starts with h: or html: - instead interpreting this as tags. assignments will be interpreted as attributes. anything can be separated using either commas or terminators.",
  HtmlBuilder mimic(call arguments, call) build
)

test1 = method(
  title = "blargus"
  IKanServe html(
    h:head(
      h:title(title)
    )
    h:body(
      h:h1(
        style = "font-size:1.5em"
        "Isn't this cool?")
    )
  )
)

test1 println
