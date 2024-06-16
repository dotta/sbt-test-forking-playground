class Suite3 extends munit.FunSuite {
  test("s3 - test 1") {
    val obtained = 42
    val expected = 42
    Thread.sleep(2000)
    assertEquals(obtained, expected)
  }
  test("s3 - test 2") {
    val obtained = 42
    val expected = 42
    Thread.sleep(2000)
    assertEquals(obtained, expected)
  }
}