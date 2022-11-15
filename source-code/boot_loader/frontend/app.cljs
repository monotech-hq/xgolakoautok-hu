
(ns boot-loader.frontend.app
    (:require ; Monoset modules
              [pathom.api]
              [developer-tools.api :as developer-tools]
              [x.boot-loader.api   :as x.boot-loader]

              ; App modules
              [app.api]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app-structure
  [ui-structure]
  [:<> [ui-structure]
       [developer-tools/magic-button]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-app!  [] (x.boot-loader/start-app!  #'app-structure))
(defn render-app! [] (x.boot-loader/render-app! #'app-structure))
