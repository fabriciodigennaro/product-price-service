package com.example.productservice.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


class HexagonalArchitectureTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
        .withImportOption(DO_NOT_INCLUDE_TESTS)
        .importPackages("com.example.productservice");

    @Test
    void domainShouldNotAccessOtherLayers() {
        classes()
            .that().resideInAPackage("..domain..")
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                "..domain..",
                "java..",
                "javax..",
                "lombok.."
            )
            .check(importedClasses);
    }

    @Test
    void applicationLayerShouldNotDependOnInfrastructureOrLibraries() {
        classes()
            .that().resideInAPackage("..application..")
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                "..application..",
                "..domain..",
                "java..",
                "javax..",
                "lombok.."
            )
            .check(importedClasses);
    }
}
