
(ns boot-loader.frontend.site
    (:require ; Monoset modules
              [pathom.api]
              [developer-tools.api :as developer-tools]
              [x.boot-loader.api   :as x.boot-loader]

              ; Site modules
              [site.pages.api]
              [site.modules.frontend.api :as modules]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- site-structure
  [ui-structure]
  [:<> [modules/wrapper ui-structure]
       [developer-tools/magic-button]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-site!  [] (x.boot-loader/start-app!  #'site-structure))
(defn render-site! [] (x.boot-loader/render-app! #'site-structure))
