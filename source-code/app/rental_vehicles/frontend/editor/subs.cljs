
(ns app.rental-vehicles.frontend.editor.subs
    (:require [app.rental-vehicles.mid.handler.helpers :as handler.helpers]
              [re-frame.api                            :as r :refer [r]]
              [x.router.api                            :as x.router]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn get-vehicle-public-link
  [db _]
  (let [vehicle-name (get-in db [:rental-vehicles :editor/edited-item :name])
        public-link  (handler.helpers/vehicle-public-link vehicle-name)]
       (r x.router/use-app-domain db public-link)))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-sub :rental-vehicles.editor/get-vehicle-public-link get-vehicle-public-link)
