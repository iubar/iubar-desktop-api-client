# iubar-desktop-api-client
Iubar desktop application api java client<br>
[![Build Status](https://app.travis-ci.com/iubar/iubar-desktop-api-client.svg?branch=master)](https://app.travis-ci.com/github/iubar/iubar-desktop-api-client)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6cfdbf2ea9144417839948336aabddde?branch=master)](https://app.codacy.com/manual/Iubar/iubar-desktop-api-client/dashboard)


## Jersey

* https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/user-guide.html
* https://howtodoinjava.com/jersey/jersey-restful-client-examples/
* https://howtodoinjava.com/jersey/jax-rs-jersey-jsonp-example/

### Jersey from 2.x to 3.x

For the next release, we continue to be working on Jersey 2.x, and we started to work on Jersey 3.x. 
Jersey 2.x continues to be compatible with JAX-RS 2.1.6 API, and we plan to keep it working with JDK 8, JDK 11, JDK 12, JDK 13, and we also plan to support JDK 14 as soon as possible. 
Jersey 3.x will be compatible with Jakarta RESTful WebServices 3.x.y API, which no longer uses javax.ws.rs java package, but it moves to jakarta.ws.rs package, similarly to all Jakarta EE 9 projects. 
Jersey 3.0.0 still will work with JDK 8, but it is possible that Jakarta RESTful WebServices API 3.1.0 or later will require JDK 11 features, and at that point, the backward compatibility with JDK 8 can be abandoned by Jersey 3.x


(from http://blog.supol.cz/?p=190)
