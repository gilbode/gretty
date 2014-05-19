/*
 * gretty
 *
 * Copyright 2013  Andrey Hihlovskiy.
 *
 * See the file "license.txt" for copying and usage permission.
 */
package org.akhikhl.gretty

import org.gradle.process.JavaForkOptions

/**
 *
 * @author akhikhl
 */
class FarmBeforeIntegrationTestTask extends FarmStartTask {

  FarmBeforeIntegrationTestTask() {
    doFirst {
      getWebAppProjects().each {
        it.tasks.each { t ->
          if(t instanceof JettyBeforeIntegrationTestTask || t instanceof JettyAfterIntegrationTestTask)
            t.enabled = false
        }
      }
    }
  }

  String getEffectiveIntegrationTestTask() {
    farm.integrationTestTask ?: new FarmConfigurer(project).getProjectFarm(farmName).integrationTestTask
  }

  @Override
  protected boolean getIntegrationTest() {
    true
  }

  void setupIntegrationTestTaskDependencies() {
    def thisTask = this
    getWebAppProjects().each { proj ->
      proj.tasks.all { t ->
        if(t.name == thisTask.effectiveIntegrationTestTask) {
          t.mustRunAfter thisTask
          thisTask.mustRunAfter proj.tasks.testClasses
          if(t instanceof JavaForkOptions && !t.ext.has('setGrettyPort')) {
            t.ext.setGrettyPort = true
            t.doFirst {
              systemProperty 'gretty.port', System.getProperty('gretty.port')
            }
          }
        } else if(t instanceof JettyBeforeIntegrationTestTask)
          t.mustRunAfter thisTask
      }
    }
  }
}
