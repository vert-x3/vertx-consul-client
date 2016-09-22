package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Services extends ChecksBase {

    @Test
    public void createLocalService() {
        ServiceOptions service = new ServiceOptions()
                .setName("serviceName")
                .setTags(Arrays.asList("tag1", "tag2"))
                .setCheckOptions(CheckOptions.ttl("10s"))
                .setAddress("10.0.0.1")
                .setPort(8080);
        runAsync(h -> writeClient.registerService(service, h));

        List<ServiceInfo> services = getAsync(h -> writeClient.localServices(h));
        ServiceInfo s = services.stream().filter(i -> "serviceName".equals(i.getName())).findFirst().get();
        String serviceId = s.getId();
        assertEquals(s.getTags().get(1), "tag2");
        assertEquals(s.getAddress(), "10.0.0.1");
        assertEquals(s.getPort(), 8080);

        List<CheckInfo> checks = getAsync(h -> writeClient.localChecks(h));
        CheckInfo c = checks.stream().filter(i -> "serviceName".equals(i.getServiceName())).findFirst().get();
        assertEquals(c.getId(), "service:serviceName");

        runAsync(h -> writeClient.deregisterService(serviceId, h));
    }

    @Test
    public void findConsul() {
        List<ServiceInfo> services = getAsync(h -> writeClient.infoService("consul", h));
        long cnt = services.stream().filter(s -> s.getName().equals("consul")).count();
        assertEquals(cnt, 1);
    }

    @Test
    public void maintenanceMode() {
        String serviceId = "serviceId";
        ServiceOptions service = new ServiceOptions()
                .setName("serviceName")
                .setId(serviceId)
                .setAddress("10.0.0.1")
                .setCheckOptions(CheckOptions.ttl("1h"))
                .setPort(8080);
        runAsync(h -> writeClient.registerService(service, h));
        runAsync(h -> writeClient.passCheck(new CheckOptions().setId("service:" + serviceId), h));

        List<CheckInfo> checks = getAsync(h -> writeClient.localChecks(h));
        assertEquals(1, checks.size());

        String reason = "reason!";
        MaintenanceOptions opts = new MaintenanceOptions()
                .setId(serviceId)
                .setReason(reason)
                .setEnable(true);
        runAsync(h -> writeClient.maintenanceService(opts, h));

        // TODO undocumented (?) behavior
        checks = getAsync(h -> writeClient.localChecks(h));
        assertEquals(2, checks.size());
        long cnt = checks.stream().filter(info -> info.getStatus() == CheckInfo.Status.critical).count();
        assertEquals(1, cnt);
        assertEquals(reason, checks.get(0).getNotes());

        opts.setEnable(false);
        runAsync(h -> writeClient.maintenanceService(opts, h));

        checks = getAsync(h -> writeClient.localChecks(h));
        assertEquals(1, checks.size());

        runAsync(h -> writeClient.deregisterService(serviceId, h));
    }

    @Override
    String createCheck(CheckOptions opts) {
        String serviceId = "serviceId";
        ServiceOptions service = new ServiceOptions()
                .setName("serviceName")
                .setId(serviceId)
                .setTags(Arrays.asList("tag1", "tag2"))
                .setCheckOptions(opts)
                .setAddress("10.0.0.1")
                .setPort(8080);
        runAsync(h -> writeClient.registerService(service, h));
        return "service:" + serviceId;
    }
}
