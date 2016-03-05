package nc.sumy.edu.webcontainer.sequrity;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.sequrity.interfaces.Security;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class that provides server security and access to it's resources.
 * It's find and parse .httpacces files, and make a conclusion about it.
 * @author Vinogradov Maxim
 */
public class ServerSecurity implements Security {
    public enum RuleTypes { ALL, HOST, IP, IPWITHMASK, IPWITHCIDRMASK }
    private static final String DIR   = "dir";
    private static final String DENY  = "deny";
    private static final String ALLOW = "allow";
    private static final String FROM  = "from";
    private static final String ORDER = "order";
    private static final String FILES = "files";
    private File file;
    private boolean withoutHtaccess = false;
    private Set<String> allowRules  = new HashSet<>();
    private Set<String> denyRules   = new HashSet<>();
    private Map<String, HashSet<String>> rules = new HashMap<>();
    private HttpRequest request;

    public ServerSecurity(HttpRequest request, File file) throws IOException {
        this.request = request;
        this.file = file;
        // file is not a directory, it will be checked in dispatcher
        parsingHtaccess();
    }

    @Override
    public boolean isAllow() {
        if (withoutHtaccess) {
            return true;
        } else {
            return checkAccess();
        }
    }

    private void parsingHtaccess() throws IOException {
        File htaccessFile = new File(file.getParentFile().getAbsolutePath() + "/.htaccess");
        List<String> lines;
        if (htaccessFile.exists()) {
            lines = Files.readAllLines(Paths.get(htaccessFile.getPath()), Charset.defaultCharset());
        } else {
            withoutHtaccess = true;
            return;
        }
        if (Objects.nonNull(lines)) {
            String scope = DIR;
            HashSet<String> tmp = new HashSet<>();
            for (String line : lines) {
                line = lowerCase(line);
                if (startsWith(line, DENY + FROM) ||
                        startsWith(line, ALLOW + FROM) ||
                        startsWith(line, ORDER)) {
                    tmp.add(line);
                }
                if (startsWith(line, "<" + FILES)) {
                    rules.put(scope, (HashSet)tmp.clone());
                    tmp.clear();
                    scope = line.substring(("<" + FILES +" ").length(), line.indexOf(">"));
                }
                if (startsWith(line, "</"+ FILES + ">")) {
                    rules.put(scope, (HashSet)tmp.clone());
                    tmp.clear();
                    scope = DIR;
                }
            }
            if (!tmp.isEmpty()) {
                rules.put(scope, (HashSet)tmp.clone());
            }
        }
    }

    private boolean checkAccess() {
        //if  rules contains name of file, than use this set, another - use DIR set
        Set<String> rulesSet = rules.containsKey(file.getName()) ?
                rules.get(file.getName()) :
                rules.get(DIR);

        RuleOrder order = RuleOrder.DENY_ALLOW;

        for (String rule : rulesSet) {
            if (contains(rule, FROM)) {
                // add allow and deny rules
                String ruleValue = rule.substring(indexOf(rule, FROM) + FROM.length() + 1);
                if (startsWith(rule, ALLOW)) {
                    for (String singleRule : split(ruleValue, " ")) {
                        allowRules.add(trim(singleRule));
                    }
                } else {
                    for (String singleRule : split(ruleValue, " ")) {
                        denyRules.add(trim(singleRule));
                    }
                }
            } else {
                // define order of rules
                if (containsIgnoreCase(rule, ORDER)) {
                    order = (indexOf(rule, ALLOW) < indexOf(rule, DENY)) ?
                            RuleOrder.ALLOW_DENY :
                            RuleOrder.DENY_ALLOW;
                } else {
                    order = RuleOrder.DENY_ALLOW;
                }
            }
        }
        boolean isDeny  = checkRules("deny");
        boolean isAllow = checkRules("allow");
        return (order == RuleOrder.DENY_ALLOW ?
                (isAllow || !isDeny) :
                (!(isDeny || !isAllow)));
    }

    private boolean checkRules(String type) {
        boolean result = false;
        Set<String> rules;
        if (StringUtils.equals(type, ALLOW)) {
            rules = allowRules;
            if (rules.isEmpty()) return true;
        } else {
            rules = denyRules;
            if (rules.isEmpty()) return false;
        }

        for (String rule : rules) {
            //result |= checkRule(rule);
        }
        return result;
    }

}