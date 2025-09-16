FROM gradle/develocity-test-distribution-agent:3.4

COPY --chown=gradle:root tcc-entrypoint.sh /opt/tcc-entrypoint.sh
RUN chmod +x /opt/tcc-entrypoint.sh

ENTRYPOINT ["/tini", "-e", "143", "--", "/opt/tcc-entrypoint.sh"]
