package hellogretty

import geb.spock.GebReportingSpec

class RequestResponseIT extends GebReportingSpec {

  private static int grettyPort

  void setupSpec() {
    grettyPort = System.getProperty('gretty.port') as int
  }

  def 'should get expected static page'() {
  when:
    go "http://localhost:${grettyPort}/grettyOverlay8/index.html"
  then:
    $('h1').text() == 'Hello, world!'
    $('p', 0).text() == /This is static HTML page./
    $('p', 'class': 'overlay').text() == /This custom version of the page was provided by overlay./
  }

  def 'should get expected response from servlet'() {
  when:
    go "http://localhost:${grettyPort}/grettyOverlay8/dynamic"
  then:
    $('h1').text() == 'Hello, world!'
    $('p', 0).text() == /This is dynamic HTML page generated by servlet./
    $('p', 'class': 'overlay').text() == /This custom version of the page was provided by overlay./
  }
}
