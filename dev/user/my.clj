(ns user.my
  (:require [clojure.tools.namespace.repl :as repl]
            [com.stuartsierra.component :as component]
            [defcomponent]
            [home-ht-challenge.server :as server]))

(def system nil)

(defn init []
  (alter-var-root
    #'system
    (constantly
      (defcomponent/system
        [server/http-server]
        {:file-config "config/production.edn"}))))

(defn start []
  (alter-var-root #'system component/start)
  :started)

(defn stop []
  (alter-var-root
    #'system
    (fn [s] (when s (component/stop s) nil))))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (repl/refresh :after 'user.my/go)
  :ok)
