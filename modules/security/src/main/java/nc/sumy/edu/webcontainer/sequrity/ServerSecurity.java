package nc.sumy.edu.webcontainer.sequrity;

import nc.sumy.edu.webcontainer.configuration.*;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.sequrity.interfaces.Security;
import org.apache.maven.shared.utils.StringUtils;

import java.io.File;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Class that provides server security and access to it's resources.
 * It's find and parse http-access.json files, and make a conclusion about it.
 * @author Vinogradov Maxim
 */
public class ServerSecurity implements Security {
    private final String HOST;
    private final String IP_ADDRESS;
    private final String FILE;
    private static final String ALLOW = "allow";
    private static final String CONFIG_FILE = "http-access.json";
    private final AccessRules RULES;
    private boolean access = false;

    // file is not a directory, it will be checked in dispatcher
    public ServerSecurity(HttpRequest request) {
        HOST = request.getHost();
        IP_ADDRESS = request.getIpAddress();
        FILE = substring(request.getUrn(), lastIndexOf(request.getUrn(), "/") + 1, length(request.getUrn()));
        File file = new File(request.getUrn());
        JSONAccessRulesConfiguration configuration = new JSONAccessRulesConfiguration();
        RULES = configuration.getAccessRules(file.getParentFile().getAbsolutePath() + File.separator + CONFIG_FILE);
        if (Objects.nonNull(RULES)) {
            AccessFile accessFile = findAccessFile();
            if (Objects.nonNull(accessFile)) {
                analyze((RulesContainer) accessFile);
            } else {
                analyze(RULES);
            }
        } else {
            access = true;
        }
    }

    private AccessFile findAccessFile() {
        Set<ServerAccessFile> files = RULES.getFiles();
        if (Objects.nonNull(files)) {
            for (AccessFile file : files) {
                if (StringUtils.equals(file.getName(), FILE) || file.getName().matches(FILE)) {
                    return file;
                }
            }
        }
        return null;
    }

    private void analyze(RulesContainer container) {
        boolean isAllow = false;
        boolean isDeny  = false;
        Set<String> allow = container.getAllow();
        Set<String> deny = container.getDeny();
        if (Objects.isNull(container.getOrder()) || equalsIgnoreCase(container.getOrder(), ALLOW)) {
            if (Objects.nonNull(allow)) {
                isAllow = checkRules(allow);
            }
            if (Objects.nonNull(deny)) {
                isDeny = checkRules(deny);
            }
            if (isAllow && !isDeny) {
                access = true;
            }
        } else {
            /*
            if (Objects.nonNull(deny)) {
                isDeny = checkRules(deny);
            }*/
            if (Objects.nonNull(allow)) {
                isAllow = checkRules(allow);
            }
            if (isAllow) {
                access = true;
            }
        }
    }

    private boolean checkRules(Set<String> set) {
        String hostPattern = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)" +
                "*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";
        String ipParts[] = IP_ADDRESS.split("\\.");
        if (checkFullEquals(set)) return true;
        for (String item : set) {
            if (contains(item, ".")) {
                String configIP[] = item.split("\\.");
                if (checkItemEquals(ipParts, configIP) == configIP.length) {
                    return true;
                }
            }
            if (item.matches(hostPattern) && StringUtils.equals(item, HOST)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkFullEquals(Set<String> set) {
        if (set.contains("all") || set.contains("ALL")) {
            return true;
        } else if (set.contains(IP_ADDRESS) || set.contains(HOST)) {
            return true;
        }
        return false;
    }

    private int checkItemEquals(String ipParts[], String configIP[]) {
        int counter = 0;
        for (int i = 0; i < configIP.length; i++) {
            if (StringUtils.equals(configIP[i], ipParts[i]) || StringUtils.equals(configIP[i], "*")) {
                counter++;
            } else if (contains(configIP[i], "/")) {
                String ipPartsTokens[] = configIP[i].split("/");
                if (Integer.parseInt(ipPartsTokens[0]) <= Integer.parseInt(ipParts[i]) &&
                        Integer.parseInt(ipPartsTokens[1]) >= Integer.parseInt(ipParts[i])) {
                    counter++;
                }
            }
        }
        return counter;
    }

    @Override
    public boolean isAllow() {
        return access;
    }

}