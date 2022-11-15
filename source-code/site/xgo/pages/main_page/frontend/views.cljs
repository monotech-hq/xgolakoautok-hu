
(ns site.xgo.pages.main-page.frontend.views
    (:require [site.components.frontend.api                    :as components]
              [site.xgo.pages.main-page.frontend.sections.api  :as sections]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  []
  [:div#xgo
   [sections/hero]
   [sections/categories]
   [sections/models]
   [sections/contacts]
   [:div {:style {:background "#2d2925" :padding "60px 0 15px 0"}}
    [components/credits {:theme :dark}]]])
