
(ns app.vehicle-categories.frontend.editor.subs
    (:require [normalize.api :as normalize]
              [re-frame.api         :as r :refer [r]]
              [x.router.api         :as x.router]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn get-category-public-link
  [db _]
  (let [category-name   (get-in db [:vehicle-categories :editor/edited-item :name])
        normalized-name (normalize/clean-text category-name)
        public-link     (str "/"normalized-name)]
       (r x.router/use-app-domain db public-link)))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-sub :vehicle-categories.editor/get-category-public-link get-category-public-link)
