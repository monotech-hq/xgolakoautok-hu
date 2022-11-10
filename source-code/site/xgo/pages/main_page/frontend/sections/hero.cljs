
(ns site.xgo.pages.main-page.frontend.sections.hero
    (:require [site.components.frontend.api :as components]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- hero
  []
  [:div {:class "xgo-section--body"}
        [components/scroll-icon {:style {:position "absolute" :bottom "0" :left "0"}}]])

(defn view
  []
  [:section {:id "xgo-hero"}
            [hero]])
