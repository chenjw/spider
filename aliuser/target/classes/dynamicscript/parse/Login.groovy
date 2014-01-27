package dynamicscript.parse;
import com.chenjw.parse.spi.impl.AttrPathSelector
import com.chenjw.spider.aliuser.ParserTemplate
import com.chenjw.spider.aliuser.constants.AliUserConstants


class Login extends ParserTemplate {
    def boolean enable() {
        return true;
    }

    def Map<String,Object> config() {
        return [
            "id":AliUserConstants.PAGE_LOGIN,
        ]
    }

    def Map<String,Object> rule() {
        return [

            "_csrf_token":[
                //"required":true,
                "selector": [
                    new AttrPathSelector("//form//input[@name='_csrf_token']/@value"),
                ],
            ],

            "action":[
                //"required":true,
                "selector": [
                    new AttrPathSelector("//form//input[@name='action']/@value"),
                ],
            ],

            "return":[
                //"required":true,
                "selector": [
                    new AttrPathSelector("//form//input[@name='return']/@value"),
                ],
            ],

            "_fm.l._0.a":[
                //"required":true,
                "filter": [
                    "return('junwen.chenjw')",
                ],
            ],

            "_fm.l._0.p":[
                //"required":true,
                "filter": [
                    "return('Cjw123457')",
                ],
            ],

            "event_submit_do_sso_login":[
                //"required":true,
                "selector": [
                    new AttrPathSelector("//form//input[@name='event_submit_do_sso_login']/@value"),
                ],
            ],

            "umidTokenId":[
                //"required":true,
                "selector": [
                    new AttrPathSelector("//form//input[@name='umidTokenId']/@value"),
                ],
            ],

        ]
    }
}
