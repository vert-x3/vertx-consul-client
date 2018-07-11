package io.vertx.ext.consul;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static io.vertx.ext.consul.impl.Utils.aggregateCheckStatus;
import static org.junit.Assert.assertEquals;

public class AggregateStatusTest {

  @Test
  public void ofEmpty() {
    assertEquals(aggregateCheckStatus(Collections.emptyList()), CheckStatus.PASSING);
  }

  @Test
  public void ofSingletonList() {
    assertEquals(aggregateCheckStatus(Collections.singletonList(CheckStatus.PASSING)), CheckStatus.PASSING);
    assertEquals(aggregateCheckStatus(Collections.singletonList(CheckStatus.WARNING)), CheckStatus.WARNING);
    assertEquals(aggregateCheckStatus(Collections.singletonList(CheckStatus.CRITICAL)), CheckStatus.CRITICAL);
  }

  @Test
  public void critical() {
    assertEquals(aggregateCheckStatus(Arrays.asList(CheckStatus.CRITICAL, CheckStatus.CRITICAL)), CheckStatus.CRITICAL);
    assertEquals(aggregateCheckStatus(Arrays.asList(CheckStatus.CRITICAL, CheckStatus.PASSING)), CheckStatus.CRITICAL);
    assertEquals(aggregateCheckStatus(Arrays.asList(CheckStatus.WARNING, CheckStatus.CRITICAL, CheckStatus.PASSING)), CheckStatus.CRITICAL);
    assertEquals(aggregateCheckStatus(Arrays.asList(CheckStatus.WARNING, CheckStatus.CRITICAL)), CheckStatus.CRITICAL);
  }

  @Test
  public void warning() {
    assertEquals(aggregateCheckStatus(Arrays.asList(CheckStatus.WARNING, CheckStatus.PASSING)), CheckStatus.WARNING);
    assertEquals(aggregateCheckStatus(Arrays.asList(CheckStatus.WARNING, CheckStatus.WARNING)), CheckStatus.WARNING);
  }

  @Test
  public void passing() {
    assertEquals(aggregateCheckStatus(Arrays.asList(CheckStatus.PASSING, CheckStatus.PASSING)), CheckStatus.PASSING);
  }

  @Test
  public void entry() {
    ServiceEntry entry = new ServiceEntry().setChecks(Arrays.asList(
      new Check().setStatus(CheckStatus.WARNING),
      new Check().setStatus(CheckStatus.PASSING)
    ));
    assertEquals(entry.aggregatedStatus(), CheckStatus.WARNING);
  }
}
