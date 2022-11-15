
(ns site.xgo.pages.main-page.frontend.sections.types.views
  (:require [re-frame.api :as r]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-type []
  [:div {:class "xgo-type"}])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn view []
  (let [view-props {:type @(r/subscribe [:types/selected])}]
    [:section {:id "xgo-types--container"}
     [vehicle-type view-props]]))
