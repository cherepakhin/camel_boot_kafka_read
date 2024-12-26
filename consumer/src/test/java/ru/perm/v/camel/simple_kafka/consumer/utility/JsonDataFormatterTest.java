package ru.perm.v.camel.simple_kafka.consumer.utility;

import org.apache.camel.*;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.UnitOfWork;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.perm.v.camel.simple_kafka.consumer.dto.MessageDTO;

import java.util.Map;

@SpringBootTest
class JsonDataFormatterTest {
    @Test
    void unmarshal() {

        JacksonDataFormat convertor = JsonDataFormatter.get(MessageDTO.class);
        String json = "";
        Exchange exchange = new Exchange() {
            @Override
            public <T extends Exchange> T adapt(Class<T> type) {
                return null;
            }

            @Override
            public ExchangePattern getPattern() {
                return null;
            }

            @Override
            public void setPattern(ExchangePattern pattern) {

            }

            @Override
            public Object getProperty(ExchangePropertyKey key) {
                return null;
            }

            @Override
            public <T> T getProperty(ExchangePropertyKey key, Class<T> type) {
                return null;
            }

            @Override
            public <T> T getProperty(ExchangePropertyKey key, Object defaultValue, Class<T> type) {
                return null;
            }

            @Override
            public void setProperty(ExchangePropertyKey key, Object value) {

            }

            @Override
            public Object removeProperty(ExchangePropertyKey key) {
                return null;
            }

            @Override
            public Object getProperty(String name) {
                return null;
            }

            @Override
            public Object getProperty(String name, Object defaultValue) {
                return null;
            }

            @Override
            public <T> T getProperty(String name, Class<T> type) {
                return null;
            }

            @Override
            public <T> T getProperty(String name, Object defaultValue, Class<T> type) {
                return null;
            }

            @Override
            public void setProperty(String name, Object value) {

            }

            @Override
            public Object removeProperty(String name) {
                return null;
            }

            @Override
            public boolean removeProperties(String pattern) {
                return false;
            }

            @Override
            public boolean removeProperties(String pattern, String... excludePatterns) {
                return false;
            }

            @Override
            public Map<String, Object> getProperties() {
                return Map.of();
            }

            @Override
            public Map<String, Object> getAllProperties() {
                return Map.of();
            }

            @Override
            public boolean hasProperties() {
                return false;
            }

            @Override
            public Message getIn() {
                return null;
            }

            @Override
            public Message getMessage() {
                return null;
            }

            @Override
            public <T> T getMessage(Class<T> type) {
                return null;
            }

            @Override
            public void setMessage(Message message) {

            }

            @Override
            public <T> T getIn(Class<T> type) {
                return null;
            }

            @Override
            public void setIn(Message in) {

            }

            @Override
            public Message getOut() {
                return null;
            }

            @Override
            public <T> T getOut(Class<T> type) {
                return null;
            }

            @Override
            public boolean hasOut() {
                return false;
            }

            @Override
            public void setOut(Message out) {

            }

            @Override
            public Exception getException() {
                return null;
            }

            @Override
            public <T> T getException(Class<T> type) {
                return null;
            }

            @Override
            public void setException(Throwable t) {

            }

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public boolean isTransacted() {
                return false;
            }

            @Override
            public boolean isRouteStop() {
                return false;
            }

            @Override
            public void setRouteStop(boolean routeStop) {

            }

            @Override
            public boolean isExternalRedelivered() {
                return false;
            }

            @Override
            public boolean isRollbackOnly() {
                return false;
            }

            @Override
            public void setRollbackOnly(boolean rollbackOnly) {

            }

            @Override
            public boolean isRollbackOnlyLast() {
                return false;
            }

            @Override
            public void setRollbackOnlyLast(boolean rollbackOnlyLast) {

            }

            @Override
            public CamelContext getContext() {
                return null;
            }

            @Override
            public Exchange copy() {
                return null;
            }

            @Override
            public Endpoint getFromEndpoint() {
                return null;
            }

            @Override
            public String getFromRouteId() {
                return "";
            }

            @Override
            public UnitOfWork getUnitOfWork() {
                return null;
            }

            @Override
            public String getExchangeId() {
                return "";
            }

            @Override
            public void setExchangeId(String id) {

            }

            @Override
            public long getCreated() {
                return 0;
            }
        };
// TDOO: convertor.unmarshal();
//        convertor.unmarshal();
    }
}