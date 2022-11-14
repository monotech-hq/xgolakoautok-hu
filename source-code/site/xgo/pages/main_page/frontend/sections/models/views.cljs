
(ns site.xgo.pages.main-page.frontend.sections.models.views
  (:require [re-frame.api :as r]
            [mid-fruits.normalize :as normalize]
            [site.xgo.pages.main-page.frontend.sections.categories.subs]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- models [view-props]
  [:div {:id "xgo-models"}
        (str view-props)])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn view []
  (let [view-props {:models @(r/subscribe [:db/get-item [:site :models]])}]
    [:section {:id "xgo-models--container"}
              [models view-props]]))
              ; [category-description]]]))
