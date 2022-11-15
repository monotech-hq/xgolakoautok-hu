
(ns app.vehicle-types.frontend.preview.subs
    (:require [mid-fruits.string :as string]
              [re-frame.api      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-type-outer-dimensions
  [db [_ preview-id]]
  (let [outer-width  (get-in db [:vehicle-types :preview/downloaded-items preview-id :outer-width])
        outer-length (get-in db [:vehicle-types :preview/downloaded-items preview-id :outer-length])
        outer-height (get-in db [:vehicle-types :preview/downloaded-items preview-id :outer-height])]
       (string/join [(string/use-placeholder (string/suffix outer-width  " mm") "-")
                     (string/use-placeholder (string/suffix outer-length " mm") "-")
                     (string/use-placeholder (string/suffix outer-height " mm") "-")]
                    " / " {:join-empty? false})))

(defn get-type-inner-dimensions
  [db [_ preview-id]]
  (let [inner-width  (get-in db [:vehicle-types :preview/downloaded-items preview-id :inner-width])
        inner-length (get-in db [:vehicle-types :preview/downloaded-items preview-id :inner-length])
        inner-height (get-in db [:vehicle-types :preview/downloaded-items preview-id :inner-height])]
       (string/join [(string/use-placeholder (string/suffix inner-width  " mm") "-")
                     (string/use-placeholder (string/suffix inner-length " mm") "-")
                     (string/use-placeholder (string/suffix inner-height " mm") "-")]
                    " / " {:join-empty? false})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :vehicle-types.preview/get-type-outer-dimensions get-type-outer-dimensions)
(r/reg-sub :vehicle-types.preview/get-type-inner-dimensions get-type-inner-dimensions)
