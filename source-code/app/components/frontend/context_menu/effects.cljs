
(ns app.components.frontend.context-menu.effects
    (:require [app.components.frontend.context-menu.views :as context-menu.views]
              [re-frame.api                               :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :components.context-menu/render-menu!
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ;  {:label (metamorphic-content)(opt)
  ;   :menu-items (maps in vector)
  ;    [{:label (metamorphic-content)
  ;      :on-click (metamorphic-event)(opt)
  ;      :placeholder (metamorphic-content)(opt)}]
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:components.context-menu/render-menu! {...}]
  ;
  ; @usage
  ;  [:components.context-menu/render-menu! :my-menu {...}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ menu-id menu-props]]
      [:x.ui/render-popup! :components.context-menu/view
                           {:content [context-menu.views/component menu-id menu-props]}]))
