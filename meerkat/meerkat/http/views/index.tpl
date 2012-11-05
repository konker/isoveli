<!DOCTYPE html>

<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<head>
  <meta charset="utf-8" />

  <!-- Set the viewport width to device width for mobile -->
  <!--
  <meta name="viewport" content="width=800" />
  -->

  <title>Meerkat</title>

  <!-- Included CSS Files -->
  <link rel="stylesheet" href="static/css/bootstrap.min.css">
  <link rel="stylesheet" href="static/css/meerkat.css">

  <!-- Custom Modernizr for Foundation -->
  <!--
  <script src="static/javascripts/foundation/modernizr.foundation.js"></script>
  -->
</head>

<body>
  <!-- Page Layout HTML here -->
  <header class="navbar navbar-inverse navbar-fixed-top" id="header">
    <div class="navbar-inner">
      <h1>meerkat</h1>
    </div>
  </header>
  <div class="container" id="container">
    <section class="row" id="master">
      <h2>
        <i class="icon-info-sign"></i>
        info
        <button type="button" class="btn pull-right" id="masterToggle"><i class="icon-off icon-white"></i> <span class="lbl">ON/OFF</span></button>
      </h2>
      <div class="section-body">
        <dl class="dl-horizontal">
          <dt class="status">status</dt>
          <dd class="status"><!-- [on/off] --></dd>
          <dt class="ip_address">ip address</dt>
          <dd class="ip_address"><!-- [ip.address] --></dd>
          <dt class="host">host</dt>
          <dd class="host"><!-- [host] --></dd>
          <dt class="uptime">uptime</dt>
          <dd class="uptime"><!-- [1:23] --></dd>
          <dt class="data_size">data size</dt>
          <dd class="data_size"><!-- [xMB] --></dd>
          <dt class="free_space">free space</dt>
          <dd class="free_space"><!-- [yMB] --></dd>
        </dl>
        <button type="button" class="btn refresh" id="masterRefresh"><i class="icon-refresh"></i> Refresh</button>

        <div id="latest-img">
          <a href="static/img/latest.jpg"><img src="static/img/latest.jpg"/></a>
        </div>
      </div>
    </section>
    <section class="row" id="probes">
      <h2><i class="icon-eye-open"></i> probes</h2>
      <div class="section-body">
        <!--START:PROBE -->
        <div class="probe row" id="probe.label">
          <h3>
            <i class="icon-chevron-right"></i>
            <span class="probe-label">probe.label</span>
            <button type="button" class="probeToggle btn pull-right"><i class="icon-off icon-white"></i> <span class="lbl">ON/OFF</span></button>
          </h3>
          <div class="probe-body">
            <div class="row status probe-section">
              <h4><i class="icon-info-sign"></i> info</h4>
              <dl class="dl-horizontal">
                <dt class="status">status</dt>
                <dd class="status"><!-- [on/off] --></dd>
                <dt class="interval">interval</dt>
                <dd class="interval"><!-- [interval] --></dd>
                <dt class="duration">duration</dt>
                <dd class="duration"><!-- [duration] --></dd>
              </dl>
              <button type="button" class="probeRefresh btn refresh"><i class="icon-refresh"></i> refresh</button>
            </div>
            <div class="row data probe-section">
              <h4><i class="icon-th-list"></i> data</h4>
              <!-- data table here -->
              <div class="dbody">
              </div>
            </div>
            <div class="row filters probe-section">
              <h4><i class="icon-wrench"></i> filters</h4>
              <!-- filters here -->
              <ol>
                <li></li>
              </ol>
            </div>
            <div class="row error-filters probe-section">
              <h4><i class="icon-wrench"></i> error filters</h4>
              <!-- error filters here -->
              <ol>
                <li></li>
              </ol>
            </div>
          </div>
        </div>
        <!--END:PROBE -->
      </div>
    </section>
    <section class="row" id="log">
      <h2>
        <i class="icon-list"></i> log
        <button type="button" class="logRefresh btn refresh pull-right"><i class="icon-refresh"></i> refresh</button>
      </h2>
      <div class="section-body">
        <!-- log here -->
        <pre>
        </pre>
      </div>
    </section>
  </div>
  <footer>
    <p>
    <i class="icon-hand-right"></i> Konrad Markus/HIIT &lt;<a href="mailto:konrad.markus@hiit.fi">konrad.markus@hiit.fi</a>&gt;
    </p>
  </footer>

  <!-- Included JS Files -->
  <script src="static/js/bootstrap.min.js"></script>
  <script src="static/js/jquery.js"></script>
  <script src="static/js/pure_min.js"></script>
  <script src="static/js/json-to-table.js"></script>
  <script src="static/js/meerkat.js"></script>
</body>
</html>
