
(ns app.vehicle-types.frontend.preview.subs
    (:require [re-frame.api :as r]
              [string.api   :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-type-outer-dimensions
  [db [_ preview-id]]
  (let [outer-width  (get-in db [:vehicle-types :preview/downloaded-items preview-id :outer-width])
        outer-length (get-in db [:vehicle-types :preview/downloaded-items preview-id :outer-length])
        outer-height (get-in db [:vehicle-types :preview/downloaded-items preview-id :outer-height])]
       (string/join [(string/use-placeholder (string/suffix outer-width  " mm") "n/a")
                     (string/use-placeholder (string/suffix outer-length " mm") "n/a")
                     (string/use-placeholder (string/suffix outer-height " mm") "n/a")]
                    " / " {:join-empty? false})))

(defn get-type-inner-dimensions
  [db [_ preview-id]]
  (let [inner-width  (get-in db [:vehicle-types :preview/downloaded-items preview-id :inner-width])
        inner-length (get-in db [:vehicle-types :preview/downloaded-items preview-id :inner-length])
        inner-height (get-in db [:vehicle-types :preview/downloaded-items preview-id :inner-height])]
       (string/join [(string/use-placeholder (string/suffix inner-width  " mm") "n/a")
                     (string/use-placeholder (string/suffix inner-length " mm") "n/a")
                     (string/use-placeholder (string/suffix inner-height " mm") "n/a")]
                    " / " {:join-empty? false})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :vehicle-types.preview/get-type-outer-dimensions get-type-outer-dimensions)
(r/reg-sub :vehicle-types.preview/get-type-inner-dimensions get-type-inner-dimensions)
