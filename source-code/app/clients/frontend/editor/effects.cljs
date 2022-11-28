
(ns app.clients.frontend.editor.effects
    (:require [app.clients.frontend.editor.views :as editor.views]
              [re-frame.api                      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :clients.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :clients.editor
                                                {:default-view-id :data}]
                [:clients.editor/render-editor!]]})

(r/reg-event-fx :clients.editor/render-editor!
  [:x.ui/render-surface! :clients.editor/view
                         {:content #'editor.views/view}])
