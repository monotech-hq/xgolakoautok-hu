
(ns app.website-menus.frontend.editor.effects
    (:require [app.website-menus.frontend.editor.views :as editor.views]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-menus.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :website-menus.editor
                                                {:default-view-id :data}]
                [:website-menus.editor/render-editor!]]})

(r/reg-event-fx :website-menus.editor/render-editor!
  [:x.ui/render-surface! :website-menus.editor/view
                         {:content #'editor.views/view}])
