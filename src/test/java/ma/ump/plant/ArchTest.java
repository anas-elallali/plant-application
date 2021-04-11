package ma.ump.plant;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("ma.ump.plant");

        noClasses()
            .that()
            .resideInAnyPackage("ma.ump.plant.service..")
            .or()
            .resideInAnyPackage("ma.ump.plant.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..ma.ump.plant.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
