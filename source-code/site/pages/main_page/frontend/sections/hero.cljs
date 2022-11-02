
(ns site.pages.main-page.frontend.sections.hero
    (:require [site.components.frontend.api :as components]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- hero
  []
  [:div {:class :sp-section-body}
        [components/scroll-icon {:style {:position "absolute" :bottom "0" :left "0"}}]])

(defn view
  []
  [:section {:id :sp-hero}
            [hero]])
