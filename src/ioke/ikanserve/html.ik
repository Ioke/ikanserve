
use("blank_slate")
IKanServe HtmlBuilder = BlankSlate create(fn(bs,
    bs pass = method(+args, +:attrs,
      currentMessage "<%s%:[ %s=\"%s\"%]>%[%s%]</%s>\n" format(currentMessage name, attrs, args, currentMessage name))))



let(test1, method(
    h = IKanServe HtmlBuilder
    title = "flurgus"
    h html(
      h head(h title(title)),
      h body(
        h h1(
          style: "font-size: 1.5em",
          "Isn't this cool?"
        )
      )
    )
    ),
;  test1 println
)
