
(ns site.xgo.pages.main-page.frontend.sections.categories.subs
  (:require [re-frame.api         :as r]
            [mid-fruits.normalize :as normalize]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-sub
  :category/description
  (fn [db [_]]
    (let [category-name (get-in db [:filters :category] "dynamic")]
        (get-in db [:site :categories category-name :category/description]))))

(r/reg-sub
  :categories/selected?
  (fn [db [_ category-name]]
    (-> db
        (get-in [:filters :category] "dynamic")
        (= (normalize/clean-text category-name "-+")))))
