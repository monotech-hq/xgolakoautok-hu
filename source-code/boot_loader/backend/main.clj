
(ns boot-loader.backend.main
    (:require ; Shadow-cljs
              [shadow.cljs.devtools.server :as server]
              [shadow.cljs.devtools.api    :as shadow]

              ; *
              [project.router.backend.api]
              [boot-loader.backend.app]
              [boot-loader.backend.site]
              [x.boot-loader.api :as x.boot-loader])
    (:gen-class))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-server!
  [{:keys [port] :as server-props}]
  (println)
  (println "project-emulator - Starting server on port:" (or port "@default"))
  (x.boot-loader/start-server! server-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn -main
  ; @param (list of *) args
  ;  [(integer)(opt) port]
  ;
  ; @usage
  ;  (-main 3000)
  ;
  ; @usage
  ;  java -jar {{namespace}}.jar 3000
  [& [port :as args]]
  (start-server! {:port port}))

(defn dev
  ; @param (map) options
  ;  {:port (integer)
  ;   :shadow-build (keyword)}
  ;
  ; @usage
  ;  (dev {:shadow-build :my-build})
  [{:keys [port shadow-build]}]
  (start-server! {:port port :dev-mode? true})
  (server/stop!)
  (server/start!)
  (shadow/watch shadow-build)
  (println "project-emulator - Development mode started"))
