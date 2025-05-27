package net.studio1122.changi1122.portfoliowebsite.web.project;

public interface MarkdownConst {
    String MARKDOWN_BODY_DEFAULT =
            """
            # Remarkable
            
            > Experience real-time editing with Remarkable!
            
            Click the `clear` link to start with a clean slate, or get the `permalink` to share or save your results.
            
            ---
            
            <details>
            <summary>토글 버튼 여기를 클릭하면 펼쳐집니다</summary>
            
            여기에 접었다 펼 수 있는 내용을 작성합니다. \s
            **마크다운 문법도 사용 가능**합니다.
            
            - 리스트
            - 강조
            - 코드 블록 등
            
            </details>
            
            ___
            
            
            ## Typographic replacements
            
            Enable typographer option to see result.
            
            (c) (C) (r) (R) (tm) (TM) (p) (P) +-
            
            test.. test... test..... test?..... test!....
            
            !!!!!! ???? ,,
            
            Remarkable -- awesome
            
            "Smartypants, double quotes"
            
            'Smartypants, single quotes'
            
            
            ## Emphasis
            
            **This is bold text**
            
            __This is bold text__
            
            *This is italic text*
            
            _This is italic text_
            
            ~~Deleted text~~
            
            Superscript: 19^th^
            
            Subscript: H~2~O
            
            ++Inserted text++
            
            ==Marked text==
            
            
            ## Blockquotes
            
            > Blockquotes can also be nested...
            >> ...by using additional greater-than signs right next to each other...
            > > > ...or with spaces between arrows.
            
            
            ## Lists
            
            Unordered
            
            + Create a list by starting a line with `+`, `-`, or `*`
            + Sub-lists are made by indenting 2 spaces:
              - Marker character change forces new list start:
                * Ac tristique libero volutpat at
                + Facilisis in pretium nisl aliquet
                - Nulla volutpat aliquam velit
            + Very easy!
            
            Ordered
            
            1. Lorem ipsum dolor sit amet
            2. Consectetur adipiscing elit
            3. Integer molestie lorem at massa
            
            
            1. You can use sequential numbers...
            1. ...or keep all the numbers as `1.`
            
            Start numbering with offset:
            
            57. foo
            1. bar
            
            
            ## Code
            
            Inline `code`
            
            Indented code
            
                // Some comments
                line 1 of code
                line 2 of code
                line 3 of code
            
            
            Block code "fences"
            
            ```
            Sample text here...
            ```
            
            Syntax highlighting
            
            ``` js
            var foo = function (bar) {
              return bar++;
            };
            
            console.log(foo(5));
            ```
            
            ## Tables
            
            | Option | Description |
            | ------ | ----------- |
            | data   | path to data files to supply the data that will be passed into templates. |
            | engine | engine to be used for processing templates. Handlebars is the default. |
            | ext    | extension to be used for dest files. |
            
            Right aligned columns
            
            | Option | Description |
            | ------:| -----------:|
            | data   | path to data files to supply the data that will be passed into templates. |
            | engine | engine to be used for processing templates. Handlebars is the default. |
            | ext    | extension to be used for dest files. |
            
            
            ## Links
            
            [link text](http://dev.nodeca.com)
            
            [link with title](http://nodeca.github.io/pica/demo/ "title text!")
            
            Autoconverted link https://github.com/nodeca/pica (enable linkify to see)
            
            
            ## Images
            
            ![Minion](https://octodex.github.com/images/minion.png)
            ![Stormtroopocat](https://octodex.github.com/images/stormtroopocat.jpg "The Stormtroopocat")
            
            Like links, Images also have a footnote style syntax
            
            ![Alt text][id]
            
            With a reference later in the document defining the URL location:
            
            [id]: https://octodex.github.com/images/dojocat.jpg  "The Dojocat"
            
            
            ## Footnotes
            
            Footnote 1 link[^first].
            
            Footnote 2 link[^second].
            
            Inline footnote^[Text of inline footnote] definition.
            
            Duplicated footnote reference[^second].
            
            [^first]: Footnote **can have markup**
            
                and multiple paragraphs.
            
            [^second]: Footnote text.
            
            
            ## Definition lists
            
            Term 1
            
            :   Definition 1
            with lazy continuation.
            
            Term 2 with *inline markup*
            
            :   Definition 2
            
                    { some code, part of Definition 2 }
            
                Third paragraph of definition 2.
            
            _Compact style:_
            
            Term 1
              ~ Definition 1
            
            Term 2
              ~ Definition 2a
              ~ Definition 2b
            
            
            ## Abbreviations
            
            This is HTML abbreviation example.
            
            It converts "HTML", but keep intact partial entries like "xxxHTMLyyy" and so on.
            
            *[HTML]: Hyper Text Markup Language
            
            
            ***
            
            __Advertisement :)__
            
            - __[pica](https://nodeca.github.io/pica/demo/)__ - high quality and fast image
              resize in browser.
            - __[babelfish](https://github.com/nodeca/babelfish/)__ - developer friendly
              i18n with plurals support and easy syntax.
            
            You'll like those projects! :)
            """;
}
