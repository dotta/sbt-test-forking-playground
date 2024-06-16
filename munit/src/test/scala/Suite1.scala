class Suite1 extends munit.FunSuite {
  test("s1 - test 1") {
    val obtained = 42
    val expected = 42
    Thread.sleep(2000)
    assertEquals(obtained, expected)
  }
  test("s1 - test 2") {
    val obtained = 42
    val expected = 42
    Thread.sleep(2000)
    assertEquals(obtained, expected)
  }
}