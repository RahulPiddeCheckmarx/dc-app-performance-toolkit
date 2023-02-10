package bamboogenerator.service.generator.plan;

import bamboogenerator.model.PlanInfo;
import com.atlassian.bamboo.specs.api.builders.Variable;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.builders.repository.git.GitRepository;
import com.atlassian.bamboo.specs.builders.task.CheckoutItem;
import com.atlassian.bamboo.specs.builders.task.ScriptTask;
import com.atlassian.bamboo.specs.builders.task.TestParserTask;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.model.task.TestParserTaskProperties;

import java.util.List;
import java.util.stream.Collectors;

import static bamboogenerator.service.generator.plan.InlineBodies.BODY_FAIL;
import static bamboogenerator.service.generator.plan.InlineBodies.BODY_SUCCESS;
import com.atlassian.bamboo.specs.util.MapBuilder;
import com.atlassian.bamboo.specs.api.builders.task.AnyTask;
import com.atlassian.bamboo.specs.api.builders.AtlassianModule;

public class PlanGenerator {
    private static final int TEST_COUNT = 1000;
    private static final String RESULT_NAME_FAIL = "failed.xml";
    private static final String RESULT_NAME_SUCCESS = "success.xml";

    public static List<Plan> generate(List<PlanInfo> planInfoList) {
        return planInfoList.stream()
                .map(PlanGenerator::createPlan)
                .collect(Collectors.toList());
    }

    private static Plan createPlan(PlanInfo planInfo) {
        boolean isFailedPlan = planInfo.isFailed();
        return new Plan(new Project().name(planInfo.getProjectName())
                .key(planInfo.getProjectKey()), planInfo.getPlanName(), planInfo.getPlanKey())
                .description("DCAPT Bamboo test build plan")
                .planRepositories(new GitRepository()
                        .name("JavaVulnerableLabE-test-repo")
                        .url("https://github.com/vbarhate/JavaVulnerableLabE.git")
                        .branch("master"))
                .variables(new Variable("stack_name", ""))
                .stages(new Stage("Stage 1")
                        .jobs(new Job("Job 1", "JB1")
                                .tasks(
                                        new VcsCheckoutTask()
                                                .description("Checkout repository task")
                                                .cleanCheckout(true)
                                                .checkoutItems(new CheckoutItem()
                                                        .repository("JavaVulnerableLabE-test-repo").path("JavaVulnerableLabE-test-repo")),
										new AnyTask(new AtlassianModule("com.cx.checkmarx-bamboo-plugin:checkmarx"))
                                    .description("SAST")
                                    .configuration(new MapBuilder()
                                            .put("cxOsaInstallBeforeScan", "")
                                            .put("cxDependencyScanFilterPatterns", "")
                                            .put("cxOsaArchiveIncludePatterns", "*.zip, *.tgz, *.war, *.ear")
                                            .put("password", "6SSTqKJm4iU=")
                                            .put("cxScaWebAppUrl", "https://sca.checkmarx.net")
                                            .put("globalCxScaResolverEnabled", "")
                                            .put("serverUrl", "")
                                            .put("scanTimeoutInMinutes", "")
                                            .put("generatePDFReport", "")
                                            .put("forceScan", "")
                                            .put("intervalBegins", "01:00")
                                            .put("enableDependencyScan", "")
                                            .put("cxScaAPIUrl", "https://api-sca.checkmarx.net")
                                            .put("cxDependencyScanfolderExclusions", "")
                                            .put("enableProxy", "")
                                            .put("highThreshold", "")
                                            .put("cxAccessControlServerUrl", "https://platform.checkmarx.net")
                                            .put("osaThresholdsEnabled", "")
                                            .put("presetName", "Checkmarx Default")
                                            .put("intervalEnds", "04:00")
                                            .put("scanControlSection", "globalConfigurationControl")
                                            .put("filterPatterns", "!**/_cvs/**/*, !**/.svn/**/*,   !**/.hg/**/*,   !**/.git/**/*,  !**/.bzr/**/*, !**/bin/**/*,!**/obj/**/*,  !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr,     !**/*.iws,   !**/*.bak,     !**/*.tmp,       !**/*.aac,      !**/*.aif,      !**/*.iff,     !**/*.m3u,   !**/*.mid,   !**/*.mp3,  !**/*.mpa,     !**/*.ra,        !**/*.wav,      !**/*.wma,      !**/*.3g2,     !**/*.3gp,   !**/*.asf,   !**/*.asx,  !**/*.avi,     !**/*.flv,       !**/*.mov,      !**/*.mp4,      !**/*.mpg,     !**/*.rm,    !**/*.swf,   !**/*.vob,  !**/*.wmv,     !**/*.bmp,       !**/*.gif,      !**/*.jpg,      !**/*.png,     !**/*.psd,   !**/*.tif,   !**/*.swf,  !**/*.jar,     !**/*.zip,       !**/*.rar,      !**/*.exe,      !**/*.dll,     !**/*.pdb,   !**/*.7z,    !**/*.gz,   !**/*.tar.gz,  !**/*.tar,       !**/*.gz,       !**/*.ahtm,     !**/*.ahtml,   !**/*.fhtml, !**/*.hdm,   !**/*.hdml,    !**/*.hsql,      !**/*.ht,       !**/*.hta,      !**/*.htc,     !**/*.htd,   !**/*.war,   !**/*.ear,  !**/*.htmls,   !**/*.ihtml,     !**/*.mht,      !**/*.mhtm,     !**/*.mhtml,   !**/*.ssi,   !**/*.stm,   !**/*.stml,    !**/*.ttml,      !**/*.txn,      !**/*.xhtm,     !**/*.xhtml,   !**/*.class, !**/*.iml,   !**/Checkmarx/Reports/**/* , !**/node_modules/**/*")
                                            .put("projectName", planInfo.getProjectName())
                                            .put("presetId", "36")
                                            .put("cxDependencySettingsCustom", "")
                                            .put("cxScaUsername", "SubhadraS")
                                            .put("folderExclusions", "")
                                            .put("osaMediumThreshold", "")
                                            .put("enableSASTScan", "true")
                                            .put("serverCredentialsSection", "globalConfigurationServer")
                                            .put("osaHighThreshold", "")
                                            .put("teamPathName", "\\CxServer")
                                            .put("cxSastSection", "globalConfigurationCxSAST")
                                            .put("cxScaPassword", "hx4qYpgOBy7lQLNnvUvBDA==")
                                            .put("osaLowThreshold", "")
                                            .put("mediumThreshold", "")
                                            .put("lowThreshold", "")
                                            .put("enablePolicyViolations", "")
                                            .put("teamPathId", "1")
                                            .put("cxScaAccountName", "plugins")
                                            .put("isIncremental", "")
                                            .put("comment", "")
                                            .put("isSynchronous", "true")
                                            .put("dependencyScanType", "AST_SCA")
                                            .put("isIntervals", "")
                                            .put("thresholdsEnabled", "")
                                            .put("username", "")
                                            .build()),
											new AnyTask(new AtlassianModule("com.cx.checkmarx-bamboo-plugin:checkmarx"))
                                    .description("SCA")
                                    .configuration(new MapBuilder()
                                            .put("cxOsaInstallBeforeScan", "")
                                            .put("cxDependencyScanFilterPatterns", "")
                                            .put("cxOsaArchiveIncludePatterns", "*.zip, *.tgz, *.war, *.ear")
                                            .put("password", "6SSTqKJm4iU=")
                                            .put("cxScaWebAppUrl", "https://sca.checkmarx.net")
                                            .put("globalCxScaResolverEnabled", "")
                                            .put("serverUrl", "")
                                            .put("scanTimeoutInMinutes", "")
                                            .put("generatePDFReport", "")
                                            .put("forceScan", "")
                                            .put("intervalBegins", "01:00")
                                            .put("enableDependencyScan", "true")
                                            .put("cxScaAPIUrl", "https://api-sca.checkmarx.net")
                                            .put("cxDependencyScanfolderExclusions", "")
                                            .put("enableProxy", "")
                                            .put("highThreshold", "")
                                            .put("cxAccessControlServerUrl", "https://platform.checkmarx.net")
                                            .put("osaThresholdsEnabled", "")
                                            .put("presetName", "Checkmarx Default")
                                            .put("intervalEnds", "04:00")
                                            .put("scanControlSection", "globalConfigurationControl")
                                            .put("filterPatterns", "!**/_cvs/**/*, !**/.svn/**/*,   !**/.hg/**/*,   !**/.git/**/*,  !**/.bzr/**/*, !**/bin/**/*,!**/obj/**/*,  !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr,     !**/*.iws,   !**/*.bak,     !**/*.tmp,       !**/*.aac,      !**/*.aif,      !**/*.iff,     !**/*.m3u,   !**/*.mid,   !**/*.mp3,  !**/*.mpa,     !**/*.ra,        !**/*.wav,      !**/*.wma,      !**/*.3g2,     !**/*.3gp,   !**/*.asf,   !**/*.asx,  !**/*.avi,     !**/*.flv,       !**/*.mov,      !**/*.mp4,      !**/*.mpg,     !**/*.rm,    !**/*.swf,   !**/*.vob,  !**/*.wmv,     !**/*.bmp,       !**/*.gif,      !**/*.jpg,      !**/*.png,     !**/*.psd,   !**/*.tif,   !**/*.swf,  !**/*.jar,     !**/*.zip,       !**/*.rar,      !**/*.exe,      !**/*.dll,     !**/*.pdb,   !**/*.7z,    !**/*.gz,   !**/*.tar.gz,  !**/*.tar,       !**/*.gz,       !**/*.ahtm,     !**/*.ahtml,   !**/*.fhtml, !**/*.hdm,   !**/*.hdml,    !**/*.hsql,      !**/*.ht,       !**/*.hta,      !**/*.htc,     !**/*.htd,   !**/*.war,   !**/*.ear,  !**/*.htmls,   !**/*.ihtml,     !**/*.mht,      !**/*.mhtm,     !**/*.mhtml,   !**/*.ssi,   !**/*.stm,   !**/*.stml,    !**/*.ttml,      !**/*.txn,      !**/*.xhtm,     !**/*.xhtml,   !**/*.class, !**/*.iml,   !**/Checkmarx/Reports/**/* , !**/node_modules/**/*")
                                            .put("projectName", "KTPlan - scanew - Default Job")
                                            .put("presetId", "36")
                                            .put("cxDependencySettingsCustom", "")
                                            .put("cxScaUsername", "SubhadraS")
                                            .put("folderExclusions", "")
                                            .put("osaMediumThreshold", "")
                                            .put("enableSASTScan", "")
                                            .put("serverCredentialsSection", "globalConfigurationServer")
                                            .put("osaHighThreshold", "")
                                            .put("teamPathName", "\\CxServer")
                                            .put("cxSastSection", "globalConfigurationCxSAST")
                                            .put("cxScaPassword", "hx4qYpgOBy7lQLNnvUvBDA==")
                                            .put("osaLowThreshold", "")
                                            .put("mediumThreshold", "")
                                            .put("lowThreshold", "")
                                            .put("enablePolicyViolations", "")
                                            .put("teamPathId", "1")
                                            .put("cxScaAccountName", "plugins")
                                            .put("isIncremental", "")
                                            .put("comment", "")
                                            .put("isSynchronous", "true")
                                            .put("dependencyScanType", "AST_SCA")
                                            .put("isIntervals", "")
                                            .put("thresholdsEnabled", "")
                                            .put("username", "")
                                            .build()),
											new AnyTask(new AtlassianModule("com.cx.checkmarx-bamboo-plugin:checkmarx"))
                                    .description("SAST_SCA")
                                    .configuration(new MapBuilder()
                                            .put("cxOsaInstallBeforeScan", "")
                                            .put("cxDependencyScanFilterPatterns", "")
                                            .put("cxOsaArchiveIncludePatterns", "*.zip, *.tgz, *.war, *.ear")
                                            .put("password", "6SSTqKJm4iU=")
                                            .put("cxScaWebAppUrl", "https://sca.checkmarx.net")
                                            .put("globalCxScaResolverEnabled", "")
                                            .put("serverUrl", "")
                                            .put("scanTimeoutInMinutes", "")
                                            .put("generatePDFReport", "")
                                            .put("forceScan", "")
                                            .put("intervalBegins", "01:00")
                                            .put("enableDependencyScan", "true")
                                            .put("cxScaAPIUrl", "https://api-sca.checkmarx.net")
                                            .put("cxDependencyScanfolderExclusions", "")
                                            .put("enableProxy", "")
                                            .put("highThreshold", "")
                                            .put("cxAccessControlServerUrl", "https://platform.checkmarx.net")
                                            .put("osaThresholdsEnabled", "")
                                            .put("presetName", "Checkmarx Default")
                                            .put("intervalEnds", "04:00")
                                            .put("scanControlSection", "globalConfigurationControl")
                                            .put("filterPatterns", "!**/_cvs/**/*, !**/.svn/**/*,   !**/.hg/**/*,   !**/.git/**/*,  !**/.bzr/**/*, !**/bin/**/*,!**/obj/**/*,  !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr,     !**/*.iws,   !**/*.bak,     !**/*.tmp,       !**/*.aac,      !**/*.aif,      !**/*.iff,     !**/*.m3u,   !**/*.mid,   !**/*.mp3,  !**/*.mpa,     !**/*.ra,        !**/*.wav,      !**/*.wma,      !**/*.3g2,     !**/*.3gp,   !**/*.asf,   !**/*.asx,  !**/*.avi,     !**/*.flv,       !**/*.mov,      !**/*.mp4,      !**/*.mpg,     !**/*.rm,    !**/*.swf,   !**/*.vob,  !**/*.wmv,     !**/*.bmp,       !**/*.gif,      !**/*.jpg,      !**/*.png,     !**/*.psd,   !**/*.tif,   !**/*.swf,  !**/*.jar,     !**/*.zip,       !**/*.rar,      !**/*.exe,      !**/*.dll,     !**/*.pdb,   !**/*.7z,    !**/*.gz,   !**/*.tar.gz,  !**/*.tar,       !**/*.gz,       !**/*.ahtm,     !**/*.ahtml,   !**/*.fhtml, !**/*.hdm,   !**/*.hdml,    !**/*.hsql,      !**/*.ht,       !**/*.hta,      !**/*.htc,     !**/*.htd,   !**/*.war,   !**/*.ear,  !**/*.htmls,   !**/*.ihtml,     !**/*.mht,      !**/*.mhtm,     !**/*.mhtml,   !**/*.ssi,   !**/*.stm,   !**/*.stml,    !**/*.ttml,      !**/*.txn,      !**/*.xhtm,     !**/*.xhtml,   !**/*.class, !**/*.iml,   !**/Checkmarx/Reports/**/* , !**/node_modules/**/*")
                                            .put("projectName", "KTPlan - sca - Default Job")
                                            .put("presetId", "36")
                                            .put("cxDependencySettingsCustom", "")
                                            .put("cxScaUsername", "SubhadraS")
                                            .put("folderExclusions", "")
                                            .put("osaMediumThreshold", "")
                                            .put("enableSASTScan", "true")
                                            .put("serverCredentialsSection", "globalConfigurationServer")
                                            .put("osaHighThreshold", "")
                                            .put("teamPathName", "\\CxServer")
                                            .put("cxSastSection", "globalConfigurationCxSAST")
                                            .put("cxScaPassword", "hx4qYpgOBy7lQLNnvUvBDA==")
                                            .put("osaLowThreshold", "")
                                            .put("mediumThreshold", "")
                                            .put("lowThreshold", "")
                                            .put("enablePolicyViolations", "")
                                            .put("teamPathId", "1")
                                            .put("cxScaAccountName", "plugins")
                                            .put("isIncremental", "")
                                            .put("comment", "")
                                            .put("isSynchronous", "true")
                                            .put("dependencyScanType", "AST_SCA")
                                            .put("isIntervals", "")
                                            .put("thresholdsEnabled", "")
                                            .put("username", "")
                                            .build()),
                                        new ScriptTask()
                                                .description("Run Bash code")
                                                .interpreterBinSh()
                                                .inlineBody("for i in $(seq 1 1000); do date=$(date -u); echo $date >> results.log; echo $date; sleep 0.06; done"),
                                        
										new ScriptTask()
                                                .description("Write XML test results")
                                                .interpreterBinSh()
                                                .inlineBody(isFailedPlan
                                                        ? String.format(BODY_FAIL, TEST_COUNT, TEST_COUNT, TEST_COUNT)
                                                        : String.format(BODY_SUCCESS, TEST_COUNT, TEST_COUNT))
                                )
                                .finalTasks(new TestParserTask(TestParserTaskProperties.TestType.JUNIT)
                                        .description("Unit test results parser task")
                                        .resultDirectories(isFailedPlan ? RESULT_NAME_FAIL : RESULT_NAME_SUCCESS)
                                )
                                .artifacts(new Artifact("Test Reports")
                                        .location(".")
                                        .copyPattern("*.log"))));
    }
}
