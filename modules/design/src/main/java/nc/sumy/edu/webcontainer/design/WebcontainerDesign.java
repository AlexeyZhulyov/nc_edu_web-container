package nc.sumy.edu.webcontainer.design;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.PaperSize;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import static com.structurizr.model.Location.External;
import static com.structurizr.model.Location.Internal;

/**
 * Describe C4 software architecture model for this simple Web Container
 *
 *
 * <h3> С4 model </h3>
 * A software system is made up of one or more containers
 * (web applications, mobile apps, standalone applications, databases, file systems, etc),
 * each of which contains one or more components, which in turn are implemented by one or more classes.
 *
 * Visualising this hierarchy is then done by creating a collection of
 * - System Context
 * - Container
 * - Component
 * - Class diagrams (optionally).
 *
 * @author  dgroup
 * @since   3/6/2016
 */
public class WebcontainerDesign {

    private static final int WORKSPACE_ID = 9421;

    /**
     * Publish C4 model to public Structurizr repository
     **/
    public static void main(String[] args) throws Exception {

        Workspace workspace = new Workspace(":Workspace name:", "Simple Java-based web server.");
        Model model = workspace.getModel();



        // create a model and the software system we want to describe
        SoftwareSystem webServer = model.addSoftwareSystem(Internal, "Name",
            ":Name: is simple web server for educational purposes. " +
            "It supports static resources (*.html), servlets and cgi calls");



        // create the various types of people (roles) that use the software system
        Person anonymousUser = model.addPerson(External, "Anonymous User", "Anybody on the web.");
        anonymousUser.uses(webServer, "View, manipulate with different web resources hosted on web server.");

        Person authenticatedUser = model.addPerson(External, "Developer User", "Development of new resources.");
        authenticatedUser.uses(webServer, "Develop, deploy and hosting of resources.");

        Person adminUser = model.addPerson(External, "Administration User", "A system administration user.");
        adminUser.uses(webServer, "Server configuration, maintenance and support.");



        // create the various software systems that web server has a dependency on
        SoftwareSystem gitHub = model.addSoftwareSystem(External, "GitHub", "github.com");
        webServer.uses(gitHub, "Gets information about public code repositories from.");

        SoftwareSystem travisCI = model.addSoftwareSystem(External, "Travis CI", "Continuous Integration server.");
        webServer.uses(travisCI, "Build smoke testing.");

        SoftwareSystem coveralls = model.addSoftwareSystem(External, "Coveralls.io", "Track code coverage over time.");
        webServer.uses(coveralls, "Gets information of code coverage.");

        SoftwareSystem codacy = model.addSoftwareSystem(External, "Codacy", "Automatic code metric");
        webServer.uses(codacy, "Static analysis, code coverage and metrics for Ruby, JavaScript, PHP, Scala, Java.");




        // now create the system context view based upon the model
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(webServer);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();



        // tag and style some elements
        webServer.addTags("Name");

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle(Tags.ELEMENT).width(600).height(450).color("#ffffff").fontSize(40);
        styles.addElementStyle("Name").background("#041F37");
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#2A4E6E");
        styles.addElementStyle(Tags.PERSON).width(575).background("#728da5");
        styles.addRelationshipStyle(Tags.RELATIONSHIP).thickness(5).fontSize(40).width(500);
        contextView.setPaperSize(PaperSize.Slide_4_3);

        StructurizrClient structurizrClient = new StructurizrClient();
        structurizrClient.mergeWorkspace(WORKSPACE_ID, workspace);
    }
}