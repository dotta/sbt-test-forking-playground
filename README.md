# sbt-test-forking-playground
A repository to explore sbt test forking

## Experiments

I'm assuming the specific test framework has no impact on the below experiments. For each experiment, the `test` task shall be run after editing the build.

### Default configuration
By default `parallelExecution` is enabled and hence the specs classes are executed in parallel. Each takes 4 seconds to execute, and as the two specs are executed in parallel the overall test execution time is also 4 seconds.

To configure the build with this setup remove `testSettings` from the `munit` project.

### Forked JVM for tests
By setting `Test / fork := true` tests execution happens in a forked JVM and all tests are executed *sequentially*. This is [expected](https://www.scala-sbt.org/1.x/docs/Testing.html#Forking+tests). The overall test execution time triples to 12 seconds.

Note that in this setup `Test / parallelExecution` is still set to `true` (as this is the default in sbt), but it has no effect. In fact, regardless of the value `Test / parallelExecution` is set to, the overall test execution time stays the same, i.e., 12 seconds.

To configure the build with this setup comment all settings in `testSettings` but `Test / fork := true`.

### Parallel test execution with forking
To go back executing test classes in parallel on a forked JVM one just needs setting `Test / testForkedParallel := true`. Doing so brings the overall test execution time back to 4 seconds. To configure the build with this setup comment `Test / parallelExecution := false` (or set it to `true`).

A surprising behavior is that if `Test / parallelExecution := false` is configured, tests classes are no longer executed in parallel despite `Test / testForkedParallel` being enabled.

To explain this behavior, I speculate that the `parallelExecution` setting has a *higher precedence* (it determines the overall execution mode of tests). When set to `false`, it effectively overrides the `testForkedParallel` setting, leading to sequential execution even when forking. When `Test / parallelExecution` is set to `false`, sbt schedules the test tasks sequentially, regardless of whether they are forked or not. Even if `Test / testForkedParallel` allows for multiple JVMs, they are not scheduled in parallel because sbt respects the sequential execution directive. This behavior can be observed by simply executing `test`  (i.e., no changes are needed in the build, as this is how the build is setup).

#### Test grouping
 Test grouping can also be combined with test forking to achieve concurrent test execution. The project's build contains an example. However, note that `concurrentRestrictions` by default contains `Limit forked-test-group to 1`, which is why `concurrentRestrictions := Seq(Tags.limit(Tags.ForkedTestGroup, 3))` would be needed to allow sbt creating three forked JVM (one for each test group).

#### Test tag with concurrent restrictions
When tests execution is forked, attempting to force sequential execution of tests by adding `Tags.limit(Tags.Test, 1)` to `concurrentRestrictions` has no effect.

## Appendix

### MUnit

MUnit does *not* support parallel execution of tests cases (i.e., tests within a spec class are executed sequentially). This is a [documented](https://scalameta.org/munit/docs/tests.html#run-tests-in-parallel) limitation. Other test frameworks (for instance, ScalaTest) may [not have such limitation](https://stackoverflow.com/questions/15752681/run-scalatest-tests-in-parallel).
