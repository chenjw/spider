package dynamicscript.parse;
import org.apache.commons.lang.StringUtils

import com.chenjw.parse.core.Context
import com.chenjw.parse.spi.Filter
import com.chenjw.spider.aliuser.ParserTemplate
import com.chenjw.spider.aliuser.constants.AliUserConstants


class Neiwang extends ParserTemplate {
    def boolean enable() {
        return true;
    }

    def Map<String,Object> config() {
        return [
            "id":AliUserConstants.PAGE_NW,
        ]
    }

    def Map<String,Object> rule() {
        return [
            "basicGroup":[
                "required":true,
                "type":"group",
                "selector": [
                    "//*[contains(text(),'基本信息')]/../..",
                ],
            ],
            "name":[
                "parent":"basicGroup",
                "required":true,
                "selector": [
                    "ul/li[1]/div",
                ],
                "filter":[
                    "remove('(查看团队)')",
                    "trim",
                ]
            ],

            "englishName":[
                "parent":"basicGroup",
                //"required":true,
                "selector": [
                    "ul/li[2]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],

            "tbName":[
                "parent":"basicGroup",
                "selector": [
                    "ul/li[3]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],

            "sex":[
                //"required":true,
                "parent":"basicGroup",

                "selector": [
                    "ul/li[4]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "workNum":[
                //"required":true,
                "parent":"basicGroup",

                "selector": [
                    "ul/li[5]/div",
                ],
                "filter":[
                    "number",
                ]
            ],
            "birthDate":[
                //"required":true,
                "parent":"basicGroup",

                "selector": [
                    "ul/li[6]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "post":[
                "required":true,
                "parent":"basicGroup",

                "selector": [
                    "ul/li[7]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "constellation":[
                "parent":"basicGroup",
                "selector": [
                    "ul/li[8]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "hrg":[
                "parent":"basicGroup",
                "selector": [
                    "ul/li[9]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],

            "actualDirector":[
                "parent":"basicGroup",

                "selector": [
                    "ul/li[10]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],


            "entryDate":[
                "required":true,
                "parent":"basicGroup",

                "selector": [
                    "ul/li[11]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "virtualDirector":[
                "parent":"basicGroup",

                "selector": [
                    "ul/li[12]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "department":[
                "parent":"basicGroup",

                "selector": [
                    "ul/li[13]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],

            "contactGroup":[
                "required":true,
                "type":"group",
                "selector": [
                    "//*[contains(text(),'联系信息')]/../..",
                ],
            ],

            "internalTel":[
                "parent":"contactGroup",
                "selector": [
                    "ul/li[1]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "mobile":[
                "parent":"contactGroup",
                "selector": [
                    "ul/li[3]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],

            "location":[
                "parent":"contactGroup",
                "selector": [
                    "ul/li[5]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "workStation":[
                "parent":"contactGroup",
                "selector": [
                    "ul/li[6]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "wwB2b":[
                "parent":"contactGroup",
                "selector": [
                    "ul/li[7]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "laiwang":[
                "parent":"contactGroup",
                "selector": [
                    "ul/li[8]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "wwTb":[
                "parent":"contactGroup",
                "selector": [
                    "ul/li[9]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],

            "sinaWeibo":[
                "parent":"contactGroup",
                "selector": [
                    "ul/li[10]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "email":[
                "parent":"contactGroup",
                "selector": [
                    "ul/li[11]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],

            "otherGroup":[
                "required":true,
                "type":"group",
                "selector": [
                    "//*[contains(text(),'其他信息')]/../..",
                ],
            ],

            "alipayAccount":[
                "parent":"otherGroup",
                "selector": [
                    "ul/li[1]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "hometown":[
                "parent":"otherGroup",
                "selector": [
                    "ul/li[2]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "myspace":[
                "parent":"otherGroup",
                "selector": [
                    "ul/li[3]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],
            "saySomething":[
                "parent":"otherGroup",
                "selector": [
                    "ul/li[4]/div",
                ],
                "filter":[
                    "trim",
                ]
            ],

        ]

    }
}
