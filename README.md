Falcon
======

Falcon is a collection of modules which help build cloud services (SaaS).

Current modules:
* cache
* common
* loadtest
* mq
* mstatic
* orm
* xss

Cache
-----

cache/src/main/java/
└── net
    └── duckling
        └── falcon
            └── api
                ├── cache
                │   └── impl
                │       └── provider
                └── idg
                    └── impl

Common
------

common/src/main/java/
└── net
    └── duckling
        ├── common
        │   └── util
        └── falcon
            └── api
                └── serialize

Lodetest
--------

loadtest/src/main/java/
├── net
│   └── duckling
│       └── falcon
│           └── api
│               └── test
│                   ├── common
│                   ├── controller
│                   └── manager
│                       ├── list
│                       └── tomcat
└── org
    └── loadtest

MQ
--

mq/src/main/java/
└── net
    └── duckling
        └── falcon
            └── api
                ├── mq
                │   └── impl
                └── taskq
                    └── impl

Mstatic
-------

mstatic/src/main/java/
└── net
    └── duckling
        └── falcon
            └── api
                └── mstatic

ORM
---

orm/src/main/java/
└── net
    └── duckling
        └── falcon
            └── api
                ├── boostrap
                └── orm

XSS
---

xss/src/main/java/
└── net
    └── duckling
        └── falcon
            └── xss


