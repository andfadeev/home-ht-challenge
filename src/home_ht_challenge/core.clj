(ns home-ht-challenge.core
  (:require [home-ht-challenge.server :as server]
            [defcomponent]
            [com.stuartsierra.component :as component]))

(defn at-shutdown
  [f]
  (-> (Runtime/getRuntime)
      (.addShutdownHook (Thread. (bound-fn [] (f))))))

(defn -main
  [config-path]
  (let [components [server/http-server]
        system (defcomponent/system
                 components
                 {:start true
                  :file-config config-path})]
    (at-shutdown #(component/stop system))
    (while true
      (Thread/sleep 100))))
