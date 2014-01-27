package dynamicscript.parse;



import org.apache.commons.lang.StringUtils

import com.chenjw.parse.core.Context
import com.chenjw.parse.spi.ArgFilter
import com.chenjw.parse.spi.Filter
import com.chenjw.parse.spi.Tool
import com.chenjw.parse.utils.GlobalTools
import com.chenjw.spider.aliuser.ParserTemplate


class _global_tools extends ParserTemplate {
    def boolean enable() {
        return true;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        GlobalTools.setGlobalTools(tools());
    }

    protected Map<String, Tool> tools() {

        return [
            'number':new Filter() {

                public String doFilter(String input, Context context) {
                    if (input == null) {
                        return null;
                    }

                    input = input?.replaceAll('[^.\\d]', '');

                    return input;
                }
            },

            'remove':new ArgFilter() {

                protected  String doFilter(String input, Context context, String[] args){
                    if (input == null) {
                        return null;
                    }
                    return StringUtils.remove(input, args[0])
                }
            },

            'trim':new Filter() {

                public String doFilter(String input, Context context) {
                    if (input == null) {
                        return null;
                    }
                    return StringUtils.trim(input);
                }
            },

            'return':new ArgFilter() {
                protected  String doFilter(String input, Context context, String[] args){
                    return args[0];
                }
            },
        ];
    }
}
