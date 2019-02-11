(defproject home-ht-challenge "0.0.1-SNAPSHOT"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [defcomponent "0.2.2"]
                 [metosin/reitit "0.2.13"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [com.zaxxer/HikariCP "3.3.0"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.postgresql/postgresql "42.2.5"]
                 [camel-snake-kebab "0.4.0"]
                 [honeysql "0.9.4"]
                 [nilenso/honeysql-postgres "0.2.4"]
                 [org.clojure/tools.logging "0.4.1"]
                 [ch.qos.logback/logback-classic "1.1.3"
                  :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]]
  :resource-paths ["config" "resources"]
  :source-paths ["src"]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]]}
             :repl {:repl-options {:init-ns user.my}}}
  :main home-ht-challenge.core)
