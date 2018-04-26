/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.consul;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.vertx.ext.consul.RandomObjects.*;
import static io.vertx.test.core.TestUtils.randomLong;
import static io.vertx.test.core.TestUtils.randomPositiveInt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class CopyTest {

  @Test
  public void testAclToken() {
    AclToken token = randomAclToken();
    checkAclToken(token, new AclToken(token));
    checkAclToken(token, new AclToken(token.toJson()));
  }

  private void checkAclToken(AclToken expected, AclToken actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getRules(), actual.getRules());
  }

  @Test
  public void testKeyValueCopy() {
    KeyValue kv = randomKeyValue();
    checkKeyValue(kv, new KeyValue(kv));
    checkKeyValue(kv, new KeyValue(kv.toJson()));
  }

  private void checkKeyValue(KeyValue expected, KeyValue actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getKey(), actual.getKey());
    assertEquals(expected.getValue(), actual.getValue());
    assertEquals(expected.getSession(), actual.getSession());
    assertEquals(expected.getCreateIndex(), actual.getCreateIndex());
    assertEquals(expected.getFlags(), actual.getFlags());
    assertEquals(expected.getModifyIndex(), actual.getModifyIndex());
    assertEquals(expected.getLockIndex(), actual.getLockIndex());
  }

  @Test
  public void testKeyValueListCopy() {
    List<KeyValue> list = new ArrayList<>();
    list.add(randomKeyValue());
    list.add(randomKeyValue());
    KeyValueList kvList = new KeyValueList()
      .setList(list)
      .setIndex(randomLong());
    checkKeyValueList(kvList, new KeyValueList(kvList));
    checkKeyValueList(kvList, new KeyValueList(kvList.toJson()));
    List<KeyValue> shuffled = new ArrayList<>();
    shuffled.add(list.get(1));
    shuffled.add(list.get(0));
    KeyValueList kvShuffled = new KeyValueList()
      .setList(shuffled)
      .setIndex(kvList.getIndex());
    checkKeyValueList(kvList, kvShuffled);
  }

  private void checkKeyValueList(KeyValueList expected, KeyValueList actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getIndex(), actual.getIndex());
    List<KeyValue> expectedList = expected.getList();
    List<KeyValue> actualList = actual.getList();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }

  @Test
  public void testCoordinateListCopy() {
    List<Coordinate> list = new ArrayList<>();
    list.add(randomCoordinate());
    list.add(randomCoordinate());
    CoordinateList coordinateList = new CoordinateList()
      .setList(list)
      .setIndex(randomLong());
    checkCoordinateList(coordinateList, new CoordinateList(coordinateList));
    checkCoordinateList(coordinateList, new CoordinateList(coordinateList.toJson()));
    List<Coordinate> shuffled = new ArrayList<>();
    shuffled.add(list.get(1));
    shuffled.add(list.get(0));
    CoordinateList coordsShuffled = new CoordinateList()
      .setList(shuffled)
      .setIndex(coordinateList.getIndex());
    checkCoordinateList(coordinateList, coordsShuffled);
  }

  private void checkCoordinateList(CoordinateList expected, CoordinateList actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getIndex(), actual.getIndex());
    List<Coordinate> expectedList = expected.getList();
    List<Coordinate> actualList = actual.getList();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }

  @Test
  public void testServiceCopy() {
    Service service = randomService();
    checkService(service, new Service(service));
    checkService(service, new Service(service.toJson()));
    List<String> tagsSuffled = new ArrayList<>();
    tagsSuffled.add(service.getTags().get(1));
    tagsSuffled.add(service.getTags().get(0));
    Service shuffled = new Service(service)
      .setTags(tagsSuffled);
    checkService(service, shuffled);
  }

  private void checkService(Service expected, Service actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getNode(), actual.getNode());
    assertEquals(expected.getNodeAddress(), actual.getNodeAddress());
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getAddress(), actual.getAddress());
    assertEquals(expected.getPort(), actual.getPort());
    List<String> expectedList = expected.getTags();
    List<String> actualList = actual.getTags();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }

  @Test
  public void testSessionCopy() {
    Session session = randomSession();
    checkSession(session, new Session(session));
    checkSession(session, new Session(session.toJson()));
    List<String> checksShuffled = new ArrayList<>();
    checksShuffled.add(session.getChecks().get(1));
    checksShuffled.add(session.getChecks().get(0));
    Session shuffled = new Session(session)
      .setChecks(checksShuffled);
    checkSession(session, shuffled);
  }

  private void checkSession(Session expected, Session actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getNode(), actual.getNode());
    assertEquals(expected.getLockDelay(), actual.getLockDelay());
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getCreateIndex(), actual.getCreateIndex());
    assertEquals(expected.getIndex(), actual.getIndex());
    List<String> expectedList = expected.getChecks();
    List<String> actualList = actual.getChecks();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }

  @Test
  public void testSessionOptions() {
    SessionOptions opts = randomSessionOptions();
    checkSessionOptions(opts, new SessionOptions(opts));
    checkSessionOptions(opts, new SessionOptions(opts.toJson()));
    SessionOptions nullChecks = opts.setChecks(null);
    checkSessionOptions(nullChecks, new SessionOptions(nullChecks));
    checkSessionOptions(nullChecks, new SessionOptions(nullChecks.toJson()));
  }

  private void checkSessionOptions(SessionOptions expected, SessionOptions actual) {
    assertEquals(expected.getBehavior(), actual.getBehavior());
    assertEquals(expected.getChecks(), actual.getChecks());
    assertEquals(expected.getLockDelay(), actual.getLockDelay());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getNode(), actual.getNode());
    assertEquals(expected.getTtl(), actual.getTtl());
  }

  @Test
  public void testCheckCopy() {
    Check check = randomCheck();
    checkCheck(check, new Check(check));
    checkCheck(check, new Check(check.toJson()));
  }

  private void checkCheck(Check expected, Check actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getNodeName(), actual.getNodeName());
    assertEquals(expected.getNotes(), actual.getNotes());
    assertEquals(expected.getOutput(), actual.getOutput());
    assertEquals(expected.getServiceId(), actual.getServiceId());
    assertEquals(expected.getServiceName(), actual.getServiceName());
    assertEquals(expected.getStatus(), actual.getStatus());
  }

  @Test
  public void testNodeCopy() {
    Node node = randomNode();
    checkNode(node, new Node(node));
    checkNode(node, new Node(node.toJson()));
  }

  private void checkNode(Node expected, Node actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getAddress(), actual.getAddress());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getWanAddress(), actual.getWanAddress());
    assertEquals(expected.getLanAddress(), actual.getLanAddress());
  }

  @Test
  public void testCoordinateCopy() {
    Coordinate coordinate = randomCoordinate();
    checkCoordinate(coordinate, new Coordinate(coordinate));
    checkCoordinate(coordinate, new Coordinate(coordinate.toJson()));
  }

  private void checkCoordinate(Coordinate expected, Coordinate actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getNode(), actual.getNode());
    assertEquals(expected.getAdj(), actual.getAdj(), 1e-6);
    assertEquals(expected.getHeight(), actual.getHeight(), 1e-6);
    assertEquals(expected.getErr(), actual.getErr(), 1e-6);
    assertEquals(expected.getVec(), actual.getVec());
  }

  @Test
  public void testEventCopy() {
    Event event = randomEvent();
    checkEvent(event, new Event(event));
    checkEvent(event, new Event(event.toJson()));
  }

  private void checkEvent(Event expected, Event actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getPayload(), actual.getPayload());
    assertEquals(expected.getNode(), actual.getNode());
    assertEquals(expected.getService(), actual.getService());
    assertEquals(expected.getTag(), actual.getTag());
    assertEquals(expected.getVersion(), actual.getVersion());
    assertEquals(expected.getLTime(), actual.getLTime());
  }

  @Test
  public void testPreparedQueryDefinitionCopy() {
    PreparedQueryDefinition pqd = randomPreparedQueryDefinition();
    checkPreparedQueryDefinition(pqd, new PreparedQueryDefinition(pqd.toJson()));
  }

  private void checkPreparedQueryDefinition(PreparedQueryDefinition expected, PreparedQueryDefinition actual) {
    assertEquals(expected.getDcs(), actual.getDcs());
    assertEquals(expected.getDnsTtl(), actual.getDnsTtl());
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getMeta(), actual.getMeta());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getNearestN(), actual.getNearestN());
    assertEquals(expected.getPassing(), actual.getPassing());
    assertEquals(expected.getService(), actual.getService());
    assertEquals(expected.getSession(), actual.getSession());
    assertEquals(expected.getTags(), actual.getTags());
    assertEquals(expected.getTemplateRegexp(), actual.getTemplateRegexp());
    assertEquals(expected.getTemplateType(), actual.getTemplateType());
    assertEquals(expected.getToken(), actual.getToken());
  }

  @Test
  public void testServiceEntryCopy() {
    ServiceEntry entry = randomServiceEntry();
    checkServiceEntry(entry, new ServiceEntry(entry));
    checkServiceEntry(entry, new ServiceEntry(entry.toJson()));
    List<Check> shuffledChecks = new ArrayList<>();
    shuffledChecks.add(entry.getChecks().get(1));
    shuffledChecks.add(entry.getChecks().get(0));
    ServiceEntry shuffled = new ServiceEntry(entry)
      .setChecks(shuffledChecks);
    checkServiceEntry(entry, shuffled);
  }

  private void checkServiceEntry(ServiceEntry expected, ServiceEntry actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getNode(), actual.getNode());
    assertEquals(expected.getService(), actual.getService());
    List<Check> expectedList = expected.getChecks();
    List<Check> actualList = actual.getChecks();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }

  @Test
  public void testServiceListCopy() {
    List<Service> services = new ArrayList<>();
    services.add(randomService());
    services.add(randomService());
    ServiceList list = new ServiceList()
      .setIndex(randomPositiveInt())
      .setList(services);
    checkServiceList(list, new ServiceList(list));
    checkServiceList(list, new ServiceList(list.toJson()));
    List<Service> shuffledServices = new ArrayList<>();
    shuffledServices.add(services.get(1));
    shuffledServices.add(services.get(0));
    ServiceList shuffled = new ServiceList()
      .setIndex(list.getIndex())
      .setList(shuffledServices);
    checkServiceList(list, shuffled);
  }

  private void checkServiceList(ServiceList expected, ServiceList actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getIndex(), actual.getIndex());
    List<Service> expectedList = expected.getList();
    List<Service> actualList = actual.getList();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }

  @Test
  public void testSessionListCopy() {
    List<Session> sessions = new ArrayList<>();
    sessions.add(randomSession());
    sessions.add(randomSession());
    SessionList list = new SessionList()
      .setIndex(randomPositiveInt())
      .setList(sessions);
    checkSessionList(list, new SessionList(list));
    checkSessionList(list, new SessionList(list.toJson()));
    List<Session> shuffledSessions = new ArrayList<>();
    shuffledSessions.add(sessions.get(1));
    shuffledSessions.add(sessions.get(0));
    SessionList shuffled = new SessionList()
      .setIndex(list.getIndex())
      .setList(shuffledSessions);
    checkSessionList(list, shuffled);
  }

  private void checkSessionList(SessionList expected, SessionList actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getIndex(), actual.getIndex());
    List<Session> expectedList = expected.getList();
    List<Session> actualList = actual.getList();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }

  @Test
  public void testNodeListCopy() {
    List<Node> nodes = new ArrayList<>();
    nodes.add(randomNode());
    nodes.add(randomNode());
    NodeList list = new NodeList()
      .setIndex(randomPositiveInt())
      .setList(nodes);
    checkNodeList(list, new NodeList(list));
    checkNodeList(list, new NodeList(list.toJson()));
    List<Node> shuffledNodes = new ArrayList<>();
    shuffledNodes.add(nodes.get(1));
    shuffledNodes.add(nodes.get(0));
    NodeList shuffled = new NodeList()
      .setIndex(list.getIndex())
      .setList(shuffledNodes);
    checkNodeList(list, shuffled);
  }

  private void checkNodeList(NodeList expected, NodeList actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getIndex(), actual.getIndex());
    List<Node> expectedList = expected.getList();
    List<Node> actualList = actual.getList();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }

  @Test
  public void testEventListCopy() {
    List<Event> events = new ArrayList<>();
    events.add(randomEvent());
    events.add(randomEvent());
    EventList list = new EventList()
      .setIndex(randomPositiveInt())
      .setList(events);
    checkEventList(list, new EventList(list));
    checkEventList(list, new EventList(list.toJson()));
    List<Event> shuffledEvents = new ArrayList<>();
    shuffledEvents.add(events.get(1));
    shuffledEvents.add(events.get(0));
    EventList shuffled = new EventList()
      .setIndex(list.getIndex())
      .setList(shuffledEvents);
    checkEventList(list, shuffled);
  }

  private void checkEventList(EventList expected, EventList actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getIndex(), actual.getIndex());
    List<Event> expectedList = expected.getList();
    List<Event> actualList = actual.getList();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }

  @Test
  public void testCheckListCopy() {
    List<Check> checks = new ArrayList<>();
    checks.add(randomCheck());
    checks.add(randomCheck());
    CheckList list = new CheckList()
      .setIndex(randomPositiveInt())
      .setList(checks);
    checkCheckList(list, new CheckList(list));
    checkCheckList(list, new CheckList(list.toJson()));
    List<Check> shuffledEvents = new ArrayList<>();
    shuffledEvents.add(checks.get(1));
    shuffledEvents.add(checks.get(0));
    CheckList shuffled = new CheckList()
      .setIndex(list.getIndex())
      .setList(shuffledEvents);
    checkCheckList(list, shuffled);
  }

  private void checkCheckList(CheckList expected, CheckList actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getIndex(), actual.getIndex());
    List<Check> expectedList = expected.getList();
    List<Check> actualList = actual.getList();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }

  @Test
  public void testServiceEntryListCopy() {
    List<ServiceEntry> entries = new ArrayList<>();
    entries.add(randomServiceEntry());
    entries.add(randomServiceEntry());
    ServiceEntryList list = new ServiceEntryList()
      .setIndex(randomPositiveInt())
      .setList(entries);
    checkServiceEntryList(list, new ServiceEntryList(list));
    checkServiceEntryList(list, new ServiceEntryList(list.toJson()));
    List<ServiceEntry> shuffledServiceEntry = new ArrayList<>();
    shuffledServiceEntry.add(entries.get(1));
    shuffledServiceEntry.add(entries.get(0));
    ServiceEntryList shuffled = new ServiceEntryList()
      .setIndex(list.getIndex())
      .setList(shuffledServiceEntry);
    checkServiceEntryList(list, shuffled);
  }

  private void checkServiceEntryList(ServiceEntryList expected, ServiceEntryList actual) {
    assertEquals(expected, actual);
    assertEquals(expected.hashCode(), actual.hashCode());
    assertEquals(expected.getIndex(), actual.getIndex());
    List<ServiceEntry> expectedList = expected.getList();
    List<ServiceEntry> actualList = actual.getList();
    assertTrue(expectedList.containsAll(actualList));
    assertTrue(actualList.containsAll(expectedList));
  }
}
