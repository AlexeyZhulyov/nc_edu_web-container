package nc.sumy.edu.webcontainer.sequrity;

import nc.sumy.edu.webcontainer.configuration.*;
import nc.sumy.edu.webcontainer.configuration.security.AccessFile;
import nc.sumy.edu.webcontainer.configuration.security.AccessRules;
import nc.sumy.edu.webcontainer.configuration.security.RulesContainer;
import nc.sumy.edu.webcontainer.configuration.security.ServerAccessFile;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.sequrity.interfaces.Security;
import org.apache.maven.shared.utils.StringUtils;

import java.io.File;
import java.util.Set;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Class that provides server security and access to it's resources.
 * It's find and parse http-access.json files, and make a conclusion about it.
 * @author Vinogradov Maxim
 */
public class ServerSecurity implements Security {
    private final String host;
    private final String ipAddress;
    private final String file;
    private static final String ALLOW = "allow";
    private static final String CONFIG_FILE = "http-access.json";
    private final AccessRules rules;
    private boolean access = false;

    // file is not a directory, it will be checked in dispatcher
    public ServerSecurity(HttpRequest request) {
        host = request.getHost();
        ipAddress = request.getIpAddress();
        file = substring(request.getUrn(), lastIndexOf(request.getUrn(), "/") + 1, length(request.getUrn()));
        File file = new File(request.getUrn());
        JSONAccessRulesConfiguration configuration = new JSONAccessRulesConfiguration();
        rules = configuration.getAccessRules(file.getParentFile().getAbsolutePath() + File.separator + CONFIG_FILE);
        if (nonNull(rules)) {
            AccessFile accessFile = findAccessFile();
            if (nonNull(accessFile)) {
                analyze((RulesContainer) accessFile);
            } else {
                analyze(rules);
            }
        } else {
            access = true;
        }
    }

    private AccessFile findAccessFile() {
        Set<ServerAccessFile> files = rules.getFiles();
        if (nonNull(files)) {
            for (AccessFile file : files) {
                if (StringUtils.equals(file.getName(), this.file) || file.getName().matches(this.file)) {
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
        if (isNull(container.getOrder()) || equalsIgnoreCase(container.getOrder(), ALLOW)) {
            if (nonNull(allow)) {
                isAllow = checkRules(allow);
            }
            if (nonNull(deny)) {
                isDeny = checkRules(deny);
            }
            if (isAllow && !isDeny) {
                access = true;
            }
        } else {
            if (nonNull(allow)) {
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
        String ipParts[] = ipAddress.split("\\.");
        if (checkFullEquals(set)) return true;
        for (String item : set) {
            if (contains(item, ".")) {
                String configIP[] = item.split("\\.");
                if (checkItemEquals(ipParts, configIP) == configIP.length) {
                    return true;
                }
            }
            if (item.matches(hostPattern) && StringUtils.equals(item, host)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkFullEquals(Set<String> set) {
        if (set.contains("all") || set.contains("ALL")) {
            return true;
        } else if (set.contains(ipAddress) || set.contains(host)) {
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
                String ipPartsTokens[] = split(configIP[i], ("/"));
                if (parseInt(ipPartsTokens[0]) <= parseInt(ipParts[i]) &&
                        parseInt(ipPartsTokens[1]) >= parseInt(ipParts[i])) {
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
