
(ns app.rental-vehicles.frontend.viewer.subs
    (:require [normalize.api :as normalize]
              [re-frame.api  :as r :refer [r]]
              [x.router.api  :as x.router]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn get-vehicle-public-link
  [db _]
  (let [vehicle-name    (get-in db [:rental-vehicles :viewer/viewed-item :name])
        normalized-name (normalize/clean-text vehicle-name)
        public-link     (str "/"normalized-name)]
       (r x.router/use-app-domain db public-link)))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-sub :rental-vehicles.viewer/get-vehicle-public-link get-vehicle-public-link)
