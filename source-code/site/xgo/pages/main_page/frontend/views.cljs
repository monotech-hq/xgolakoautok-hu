
(ns site.xgo.pages.main-page.frontend.views
    (:require [re-frame.api                                    :as r]
              [reagent.api                                     :as reagent]
              [site.components.frontend.api                    :as components]
              [site.xgo.pages.main-page.frontend.sections.api  :as sections]
              [x.router.api                                    :as router]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-fx
 :scroll-on-init
 (fn [path-params]
  (when (not (empty? path-params))
    (let [element (.getElementById js/document "xgo-categories")]
      (.scrollIntoView element
        (clj->js {:block    "start"
                  :inline   "center"}))))))

(r/reg-event-fx
 :init
 (fn [{:keys [db]} [_]]
   {:scroll-on-init (router/get-current-route-path-params db)}))

(defn view
  []
  (reagent/lifecycles 
   {:component-did-mount (fn []
                          (r/dispatch [:init]))
    :reagent-render 
    (fn [] 
      [:div#xgo
        [sections/hero]
        [sections/categories]
        (if @(r/subscribe [:filters/model])
          [sections/types]
          [sections/models])
        [sections/contacts]
        [:div {:style {:background "#2d2925" :padding "60px 0 15px 0"}}
          [components/credits {:theme :dark}]]])}))
