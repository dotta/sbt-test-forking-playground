// Note that if the following `concurrentRestrictions` are set at project (munit) level,
// then `test` execution will happen sequentially. One would need to look into the sbt
// internals to understand why that is the case.
// The "Tags.ForkedTestGroup" is set to 2 to demonstrate that not all suites are
// executed concurrently.
// ThisBuild / concurrentRestrictions := Seq(Tags.limit(Tags.ForkedTestGroup, 2))
lazy val testSettings = Seq(
    Test / fork := true,
    Test / testForkedParallel := true,
    // After disabling Test / parallelExecution,
    // enabling Test / testForkedParallel has no effect.
    Test / parallelExecution := false,
    /*
    // The following configuration has the same effect of configuring `Test / testForkedParallel := true`,
    // but it provides more control on how many forked JVM are concurrently running.
    // Mind that the `concurrentRestrictions` also need to be adapted (uncomment the line with
    // `ThisBuild / concurrentRestrictions` at the top of this build file).
    Test / testGrouping := (Test / testGrouping).value.flatMap { group =>
      group.tests map (test => Tests.Group(test.name, Seq(test), Tests.SubProcess(ForkOptions())))
    },
    */
)

lazy val munit = (project in file("munit"))
  .settings(testSettings)
  .settings(
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
  )
