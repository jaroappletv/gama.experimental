package ummisco.gama.unity.skills;

import java.util.ArrayList;

import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;


import com.thoughtworks.xstream.XStream;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.GamlAnnotations.action;
import msi.gama.precompiler.GamlAnnotations.arg;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.operator;
import msi.gama.precompiler.GamlAnnotations.skill;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.IOperatorCategory;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.skills.Skill;
import msi.gaml.types.IType;
import ummisco.gama.dev.utils.DEBUG;

import ummisco.gama.serializer.factory.StreamConverter;
import ummisco.gama.serializer.gamaType.converters.ConverterScope;
import ummisco.gama.unity.messages.ColorTopicMessage;
import ummisco.gama.unity.messages.CreateTopicMessage;
import ummisco.gama.unity.messages.DestroyTopicMessage;
import ummisco.gama.unity.messages.GamaUnityMessage;
import ummisco.gama.unity.messages.GetTopicMessage;
import ummisco.gama.unity.messages.ItemAttributes;
import ummisco.gama.unity.messages.MonoActionTopicMessage;
import ummisco.gama.unity.messages.MoveTopicMessage;
import ummisco.gama.unity.messages.NotificationMessage;
import ummisco.gama.unity.messages.NotificationTopicMessage;
import ummisco.gama.unity.messages.PluralActionTopicMessage;
import ummisco.gama.unity.messages.PositionTopicMessage;
import ummisco.gama.unity.messages.PropertyTopicMessage;
import ummisco.gama.unity.messages.SetTopicMessage;
import ummisco.gama.unity.mqtt.SubscribeCallback;
import ummisco.gama.unity.mqtt.Utils;


/**
 * UnitySkill : This class is intended to define the minimal set of behaviours required from an agent that is able to
 * communicate with unity angine in order to visulaize GAMA simulations. Each member that has a meaning in GAML is annotated with the respective tags (vars, getter, setter, init,
 * action & args)
 *
 */

@doc ("The unity skill is intended to define the minimal set of behaviors required for agents that are able to communicate with the unity engine in order to visualize GAMA simulations in different terminals")
@skill(name = UnitySkill.SKILL_NAME, concept = { IConcept.NETWORK, IConcept.COMMUNICATION, IConcept.SKILL })
public class UnitySkill extends Skill {
	//
	//public static final String SKILL_NAME = "unity";
	public static final String BROKER_URL = "tcp://localhost:1883";
	//public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
	
	
	
	public static final String SKILL_NAME = "unity";
	//public static final String BROKER_URL = "tcp://195.221.248.15:1935";
	//public static final String DEFAULT_PASSWORD = "gama_demo";
	//public static final String DEFAULT_USER = "gama_demo";
	
	public static final MqttConnectOptions options = new MqttConnectOptions();

	public static MqttClient client = null;
	public static SubscribeCallback subscribeCallback = new SubscribeCallback();
	
	//public ArrayList<String> mailBox = new ArrayList<String>();

	// @Override
	public Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {
		final IAgent agent = scope.getAgent();
		return " ";
	}

	@action ( 
			name = "connectMqttClient", 
			args = { @arg ( 
							name = "idClient", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("Agent name")) }, 
			doc = @doc ( 
						value = "Generates a client ID and connects it to the Mqtt server.", 
						returns = "The client generated identifier.", 
						examples = { @example("") }))
	public static String connectMqttClient(final IScope scope) {
		String clientId = Utils.getMacAddress() + "-" + scope.getArg("idClient", IType.STRING) + "-pub";
		try {
			client = new MqttClient(BROKER_URL, clientId);
			options.setCleanSession(true);
	//		options.setPassword(DEFAULT_PASSWORD.toCharArray());
	//		options.setUserName(DEFAULT_USER);
			
			
			
		//	final MqttConnectOptions connOpts = new MqttConnectOptions();
		//	connOpts.setCleanSession(true);
			//subscribeCallback = new SubscribeCallback();
			//client.setCallback(subscribeCallback);
			//connOpts.setCleanSession(true);
			//connOpts.setKeepAliveInterval(30);
			//client.connect(connOpts);
			
			
			
			
		//	
		//	public static String DEFAULT_LOCAL_NAME = "gama-" + Calendar.getInstance().getTimeInMillis() + "@";
		//	
			
			
		//	options.setWill(client.getTopic("home/LWT"), "I'm gone :(".getBytes(), 0, false);
			client.connect(options);
			DEBUG.LOG("Client : " + clientId + " connected with success!");
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
		scope.getSimulation().postDisposeAction(scope1 -> {
			try {
				if (client.isConnected())
					client.disconnect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		});

		return clientId;
	}

	//TODO: Youcef-> Review this action to remove some attributes, make it more generic and fix data structure issues
	@action ( 
				name = "send_unity_message", 
				args = { @arg ( 
								name = "sender", 
								type = IType.STRING, 
								optional = false, 
								doc = @doc("The sender")),
						@arg ( 
								name = "objectName", 
								type = IType.STRING,
								optional = false, 
								doc = @doc("The game object name")),
						@arg ( 
								name = "attributes",
								type = IType.MAP, 
								optional = false, 
								doc = @doc( "The attribute list and their values")),
						@arg ( 
								name = "topic", 
								type = IType.STRING, 
								optional = false, 
								doc = @doc("The topic")) }, 
				doc = @doc ( 
							value = "The generic form of a message to send to Unity engine. ", 
							returns = "true if it is in the base.", 
							examples = { @example("") }))
	public static Boolean sendUnityMqttMessage(final IScope scope) 
	{
		String sender = (String) scope.getArg("sender", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		Map<String, String> attribute = (Map<String, String>) scope.getArg("attributs", IType.MAP);
		String topic = (String) scope.getArg("topic", IType.STRING);

		ArrayList<ItemAttributes> items = new ArrayList();
		for (Map.Entry<?, ?> entry : attribute.entrySet()) {
			ItemAttributes it = new ItemAttributes(entry.getKey(), entry.getValue());
			items.add(it);
		}

		GamaUnityMessage messageUnity = new GamaUnityMessage(scope, sender, objectName, "Not set", objectName, items, topic, "content");
		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(messageUnity);
		final MqttTopic unityTopic = client.getTopic(topic);
		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
			DEBUG.LOG("New message sent to Unity. Topic: " + unityTopic.getName() + "   Message: " + stringMessage);
			return true;
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	
	//TODO: Youcef-> Review this action with better description and genericity support
	@action(
			name = "getUnityField", 
			args = { @arg (
							name = "objectName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The game object name")),
					@arg (
							name = "attribute", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The field name")), }, 
			doc = @doc (
					value = "Get a unity game object field value", 
					returns = "void", 
					examples = { @example("") }))
	public static void getUnityField(final IScope scope) 
	{
		String sender = (String) scope.getAgent().getName();
		String receiver = (String) scope.getArg("objectName", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		String attribute = (String) scope.getArg("attribute", IType.STRING);

		GetTopicMessage topicMessage = new GetTopicMessage(scope, sender, receiver, objectName, attribute);
		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(topicMessage);
		final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_GET);

		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	//TODO: Youcef-> Review this action with better description and genericity support
	@action (
			name = "setUnityFields", 
			args = { @arg (
							name = "objectName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The game object name")),
					@arg (
							name = "attributes", 
							type = IType.MAP, 
							optional = false, 
							doc = @doc("The attribute list and their values")) }, 
			doc = @doc (
							value = "Set a set of fields of a unity game object.", 
							returns = "void", 
							examples = { @example("") }))
	public static void setUnityField(final IScope scope) 
	{
		String sender = (String) scope.getAgent().getName();
		String receiver = (String) scope.getArg("objectName", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		Map<String, String> attributes = (Map<String, String>) scope.getArg("attributes", IType.MAP);

		ArrayList<ItemAttributes> items = new ArrayList();
		for (Map.Entry<?, ?> entry : attributes.entrySet()) {
			ItemAttributes it = new ItemAttributes(entry.getKey(), entry.getValue());
			items.add(it);
		}

		SetTopicMessage setMessage = new SetTopicMessage(scope, sender, receiver, objectName, items);
		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(setMessage);
		final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_SET);
		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	
	//TODO: Youcef-> Review this action with better description and genericity support
	@action ( 
			name = "setUnityProperty", 
			args = { @arg ( 
							name = "objectName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The game object name")),
					@arg (
							name = "propertyName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The property name")),
					@arg ( 
							name = "propertyValue", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The property value")) }, 
			doc = @doc ( 
						value = "Set a property value.", 
						returns = "void", 
						examples = { @example("") }))
	public static void setUnityProperty(final IScope scope) 
	{
		String sender = (String) scope.getAgent().getName();
		String receiver = (String) scope.getArg("objectName", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		String propertyName = (String) scope.getArg("propertyName", IType.STRING);
		String propertyValue = (String) scope.getArg("propertyValue", IType.STRING);

		PropertyTopicMessage topicMessage = new PropertyTopicMessage(scope, sender, receiver, objectName, propertyName,
				propertyValue);
		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(topicMessage);
		final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_PROPERTY);
		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	
	//TODO: Youcef-> Review this action with better description and genericity support
	@action	( 
				name = "callUnityMonoAction", 
				args = { @arg ( 
								name = "objectName", 
								type = IType.STRING, 
								optional = false, 
								doc = @doc("The game object name")),
						@arg (
								name = "actionName", 
								type = IType.STRING, 
								optional = false, 
								doc = @doc("The game object name")),
						@arg ( 
								name = "attribute", 
								type = IType.STRING, 
								optional = false, 
								doc = @doc("The attribute list and their values")) }, 
				doc = @doc ( 
							value = "Call a unity game object method that has one parameter", 
							returns = "void", 
							examples = { @example("") }))
	public static void callUnityMonoAction(final IScope scope) 
	{
		String sender = (String) scope.getAgent().getName();
		String receiver = (String) scope.getArg("objectName", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		String actionName = (String) scope.getArg("actionName", IType.STRING);
		String attribute = (String) scope.getArg("attribute", IType.STRING);

		MonoActionTopicMessage topicMessage = new MonoActionTopicMessage(scope, sender, receiver, objectName,
				actionName, attribute);
		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(topicMessage);
		final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_MONO_FREE);
		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	//TODO: Youcef-> Review this action with better description and genericity support
	@action	(	
			name = "callUnityPluralAction", 
			args = { @arg ( 
							name = "objectName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The game object name")),
					@arg (
							name = "actionName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The game object name")),
					@arg ( 
							name = "attributes", 
							type = IType.MAP, 
							optional = false, 
							doc = @doc("The attribute list and their values")) }, 
			doc = @doc ( 
						value = "Call a unity game object method that has several parameters.",
						returns = "void", 
						examples = { @example("") }))
	public static void callUnityPluralAction(final IScope scope) 
	{
		String sender = (String) scope.getAgent().getName();
		String receiver = (String) scope.getArg("objectName", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		String actionName = (String) scope.getArg("actionName", IType.STRING);
		Map<String, String> attributes = (Map<String, String>) scope.getArg("attributes", IType.MAP);

		ArrayList<ItemAttributes> items = new ArrayList();
		for (Map.Entry<?, ?> entry : attributes.entrySet()) {
			ItemAttributes it = new ItemAttributes(entry.getKey(), entry.getValue());
			items.add(it);
		}
		PluralActionTopicMessage topicMessage = new PluralActionTopicMessage(scope, sender, receiver, objectName,
				actionName, items);
		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(topicMessage);
		final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_MULTIPLE_FREE);
		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	//TODO: Youcef-> Review this action with better description and genericity support
	@action (	
			name = "setUnityColor", 
			args = { @arg ( 
							name = "objectName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The game object name")),
					@arg ( 
							name = "color", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The color name")), }, 
			doc = @doc ( 
						value = "Set a unity game object color", 
						returns = "void", 
						examples = { @example("") }))
	public static void setUnityColor(final IScope scope) 
	{
		String sender = (String) scope.getAgent().getName();
		String receiver = (String) scope.getArg("objectName", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		String color = (String) scope.getArg("color", IType.STRING);

		ColorTopicMessage topicMessage = new ColorTopicMessage(scope, sender, receiver, objectName, color);
		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(topicMessage);
		final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_COLOR);
		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	//TODO: Youcef-> Review this action with better description and genericity support
	@action ( 
			name = "setUnityPosition", 
			args = { @arg ( 
							name = "objectName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The game object name")),
					@arg ( 
							name = "position", 
							type = IType.MAP, 
							optional = false, 
							doc = @doc("The position values (x,y,z)")), }, 
			doc = @doc ( 
						value = "Set the position of a unity game object", 
						returns = "void", 
						examples = { @example("") }))
	public static void setUnityPosition(final IScope scope) 
	{
		String sender = (String) scope.getAgent().getName();
		String receiver = (String) scope.getArg("objectName", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		Map<String, String> position = (Map<String, String>) scope.getArg("position", IType.MAP);

		ArrayList<ItemAttributes> items = new ArrayList();
		for (Map.Entry<?, ?> entry : position.entrySet()) {
			ItemAttributes it = new ItemAttributes(entry.getKey(), entry.getValue());
			items.add(it);
		}
		PositionTopicMessage topicMessage = new PositionTopicMessage(scope, sender, receiver, objectName, items);
		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(topicMessage);
		final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_POSITION);

		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	//TODO: Youcef-> Review this action with better description and genericity support
	@action	( 
			name = "unityMove", 
			args = { @arg ( 
							name = "objectName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The game object name")),
					@arg ( 
							name = "position", 
							type = IType.MAP, 
							optional = false, 
							doc = @doc("The position values (x,y,z)")),
					@arg ( 
							name = "speed", 
							type = IType.INT, 
							optional = false, 
							doc = @doc("speed")),}, 
			doc = @doc ( 
						value = "Set the position of a unity game object", 
						returns = "void", 
						examples = { @example("") }))
	public static synchronized void unityMove(final IScope scope) 
	{
		String sender = (String) scope.getAgent().getName();
		String receiver = (String) scope.getArg("objectName", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		Map<String, String> position = (Map<String, String>) scope.getArg("position", IType.MAP);
		int speed = (int) scope.getArg("speed", IType.INT);

		ArrayList<ItemAttributes> items = new ArrayList();
		for (Map.Entry<?, ?> entry : position.entrySet()) {
			ItemAttributes it = new ItemAttributes(entry.getKey(), entry.getValue());
			items.add(it);
		}
		MoveTopicMessage topicMessage = new MoveTopicMessage(scope, sender, receiver, objectName, items, speed);
		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(topicMessage);
		final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_MOVE);

		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		DEBUG.LOG("New message sent to Unity. Topic: " + unityTopic.getName() + "   Number: " + stringMessage);
	}

	//TODO: Youcef-> Review this action with better description and genericity support
	@action ( 
			name = "newUnityObject", 
			args = { @arg ( 
							name = "objectName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The game object name")),
					@arg ( 
							name = "type", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The object type")),
					@arg ( 
							name = "color", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("The object color")),
					@arg ( 
							name = "position", 
							type = IType.MAP, 
							optional = false, 
							doc = @doc("The object position")), }, 
			doc = @doc ( 
						value = "Create a new unity game object on the scene and set its initial color and position. Supported fomes are: Capsule, Cube, Cylinder and Sphere", 
						returns = "void", 
						examples = { @example("") }))
	public static synchronized void newUnityObject(final IScope scope) 
	{
		String sender = (String) scope.getAgent().getName();
		String receiver = (String) scope.getArg("objectName", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		String type = (String) scope.getArg("type", IType.STRING);
		String color = (String) scope.getArg("color", IType.STRING);
		
		Map<String, String> position = (Map<String, String>) scope.getArg("position", IType.MAP);

		ArrayList<ItemAttributes> items = new ArrayList();
		for (Map.Entry<?, ?> entry : position.entrySet()) {
			ItemAttributes it = new ItemAttributes(entry.getKey(), entry.getValue());
			items.add(it);
		}
		

		CreateTopicMessage topicMessage = new CreateTopicMessage(scope, sender, receiver, objectName, type, color,
				items);
		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(topicMessage);
		final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_CREATE_OBJECT);

		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	//TODO: Youcef-> Review this action with better description and genericity support
		@action ( 
				name = "destroyUnityObject", 
				args = { @arg ( 
								name = "objectName", 
								type = IType.STRING, 
								optional = false, 
								doc = @doc("The game object name")) }, 
				doc = @doc ( 
							value = "Destroy a unity game object", 
							returns = "void", 
							examples = { @example("") }))
		public static synchronized void destroyUnityObject(final IScope scope) 
		{
			String sender = (String) scope.getAgent().getName();
			String receiver = (String) scope.getArg("objectName", IType.STRING);
			String objectName = (String) scope.getArg("objectName", IType.STRING);
		
			DestroyTopicMessage topicMessage = new DestroyTopicMessage(scope, sender, receiver, objectName);
			XStream xstream = new XStream();
			final String stringMessage = xstream.toXML(topicMessage);
			final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_DESTROY_OBJECT);

			try {
				MqttMessage message = new MqttMessage();
				message.setPayload(stringMessage.getBytes());
				unityTopic.publish(message);
				
				DEBUG.LOG("Message Destroy sent: "+stringMessage);
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}

	//TODO: Youcef-> Review this action with better description and genericity support
	@action ( 
			name = "unityNotificationSubscribe", 
			args = { @arg ( 
							name = "objectName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc ( "The game object name" ) ),
					@arg ( 
							name = "notificationId", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc ( "notificationId: the notification ID to communicate when notifying an agent by unity")),
					@arg ( 
							name = "fieldType", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("fieldType: a field or a property in the game object")),
					@arg ( 
							name = "fieldName", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("fieldName: The field name")),
					@arg ( 
							name = "fieldValue", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("fieldValue: The field value")),
					@arg ( 
							name = "fieldOperator", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc ( "fieldOperator: The comparaison operator")), }, 
			doc = @doc ( 
						value = "Subscribe to the notification mechanism, allowing unity to notify Gama when the condition on the specified field has been met.", 
						returns = "void", 
						examples = { @example("") }))
	public synchronized void unityNotificationSubscribe(final IScope scope) 
	{
		String sender = (String) scope.getAgent().getName();
		String notificationId = (String) scope.getArg("notificationId", IType.STRING);
		String receiver = (String) scope.getArg("objectName", IType.STRING);
		String objectName = (String) scope.getArg("objectName", IType.STRING);
		String fieldType = (String) scope.getArg("fieldType", IType.STRING);
		String fieldName = (String) scope.getArg("fieldName", IType.STRING);
		String fieldValue = (String) scope.getArg("fieldValue", IType.STRING);
		String fieldOperator = (String) scope.getArg("fieldOperator", IType.STRING);

		NotificationTopicMessage topicMessage = new NotificationTopicMessage(scope, sender, receiver, notificationId,
				objectName, fieldType, fieldName, fieldValue, fieldOperator);

		XStream xstream = new XStream();
		final String stringMessage = xstream.toXML(topicMessage);
		final MqttTopic unityTopic = client.getTopic(IUnitySkill.TOPIC_NOTIFICATION);

		try {
			MqttMessage message = new MqttMessage();
			message.setPayload(stringMessage.getBytes());
			unityTopic.publish(message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	//TODO: Youcef-> Review this action with better description and genericity support
	@action ( 
			name = "disconnectMqttClient", 
			args = { @arg ( 
							name = "idClient", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("predicate name")) }, 
			doc = @doc ( 
						value = "Disconnect the client from the Mqtt server.", 
						returns = "true if it is in the base.", 
						examples = { @example("") }))
	public static String disconnectMqttClient(final IScope scope) {
		String clientId = Utils.getMacAddress() + "-" + scope.getArg("idClient", IType.STRING) + "-pub";
		try {
			
			if (client.isConnected())
				client.disconnect();
			   DEBUG.LOG("Client : " + clientId + " disconnected with success!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientId;
	}

	//TODO: Youcef-> Review this action with better description and genericity support
	@action ( 
			name = "subscribe_To_Topic", 
			args = { @arg ( 
							name = "idClient", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("Client Id")),
					@arg ( 
							name = "topic", 
							type = IType.STRING, 
							optional = false, 
							doc = @doc("Topic Name")) }, 
			doc =  @doc ( 
						value = "Subscribe a client to a topic", 
						returns = "true if success, false otherwise", 
						examples = { @example("") }))
	public String SubscribeToTopic(final IScope scope) {
		String clientId = Utils.getMacAddress() + "-" + scope.getArg("idClient", IType.STRING) + "-pub";
		final String topic = (String) scope.getArg("topic", IType.STRING);

		try {
			client.setCallback(subscribeCallback);
			// client.connect();
			client.subscribe(topic);
			DEBUG.LOG("Subscriber is now listening to " + topic);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return "Subscribed to the topic: " + topic;
	}
	
	
	//TODO Youcef-> Review this action with better description and genericity support
	@action ( 
			name = "get_unity_message", 
			doc = @doc(value = "Get the next received mqtt message.", 
			returns = "The message content if there is a received message, null otherwise.", 
			examples = { @example("") }))
	public synchronized String getUnityMessage(final IScope scope) {
		return subscribeCallback.getNextMessage();
	}

	//TODO: Youcef-> Review this action with better description and genericity support
	@action ( 
			name = "get_unity_replay", 
			doc = @doc(value = "Get the next received mqtt message.", 
			returns = "The message content if there is a received message, null otherwise.", 
			examples = { @example("") }))
	public synchronized String getReplayUnityMessage(final IScope scope) {
		return subscribeCallback.getNextReplayMessage();
	}

	//TODO: Youcef-> Review this action with better description and genericity support
	@action ( 
			name = "get_unity_notification", 
			doc = @doc(value = "Get the next received mqtt notification message.", 
			returns = "The message content if there is a received message, null otherwise.", 
			examples = { @example("") }))
	public synchronized String getUnityNotificationMessage(final IScope scope) {
		return subscribeCallback.getNextNotificationMessage();
	}

	//TODO: Youcef-> Review this action with better description and genericity support
	@operator ( 
			value = "isNotificationTrue", 
			doc = { @doc("Check if the notification has been received") }, 
			category = { IOperatorCategory.LOGIC })
	public static synchronized boolean isNotificationTrue(final IScope scope, String notificationId) {

		DEBUG.LOG("subscribeCallback.notificationMailBox.size()  is:  " + subscribeCallback.notificationMailBox.size());
		if (subscribeCallback.notificationMailBox.size() > 0) {

			for (MqttMessage msg : subscribeCallback.notificationMailBox) {
				String message = msg.toString();
				final ConverterScope cScope = new ConverterScope(scope);
				final XStream xstream = StreamConverter.loadAndBuild(cScope);
				final NotificationMessage notifMsg = (NotificationMessage) xstream.fromXML(message);

				if (notifMsg.notificationId.equals(notificationId)) {
					subscribeCallback.notificationMailBox.remove(0);
					return true;
				}
			}
		} else {
			return false;
		}
		return false;
	}

}