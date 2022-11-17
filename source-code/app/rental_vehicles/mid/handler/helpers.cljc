
(ns app.rental-vehicles.mid.handler.helpers
    (:require [normalize.api :as normalize]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn vehicle-link-name
  [vehicle-name]
  (normalize/clean-text vehicle-name))

(defn vehicle-public-link
  [vehicle-name]
  (let [link-name (vehicle-link-name vehicle-name)]
       (str "/berelheto-jarmuveink/"link-name)))
