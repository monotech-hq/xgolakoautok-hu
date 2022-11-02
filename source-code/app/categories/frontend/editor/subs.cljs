
(ns app.categories.frontend.editor.subs
    (:require [mid-fruits.normalize :as normalize]
              [re-frame.api         :as r :refer [r]]
              [x.app-router.api     :as x.router]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn get-category-public-link
  [db _]
  (let [category-name   (get-in db [:categories :editor/edited-item :name])
        normalized-name (normalize/clean-text category-name)
        public-link     (str "/"normalized-name)]
       (r x.router/use-app-domain db public-link)))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-sub :categories.editor/get-category-public-link get-category-public-link)
